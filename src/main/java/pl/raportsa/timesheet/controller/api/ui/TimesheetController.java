package pl.raportsa.timesheet.controller.api.ui;

import lombok.RequiredArgsConstructor;
import org.apache.commons.io.FileUtils;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import pl.raportsa.timesheet.model.dto.AbsenceDto;
import pl.raportsa.timesheet.model.dto.WorkDayDto;
import pl.raportsa.timesheet.model.dto.WorkDaySingleDto;
import pl.raportsa.timesheet.model.entity.Employee;
import pl.raportsa.timesheet.model.enums.WorkDaysResultType;
import pl.raportsa.timesheet.model.enums.WorkType;
import pl.raportsa.timesheet.service.EmployeeService;
import pl.raportsa.timesheet.service.MonthlyReportGenerator;
import pl.raportsa.timesheet.service.TimesheetService;
import pl.raportsa.timesheet.utils.DateUtils;
import pl.raportsa.timesheet.utils.PdfUtils;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequiredArgsConstructor
@RequestMapping("/timesheet")
public class TimesheetController {

    private final TimesheetService timeSheetService;
    private final EmployeeService employeeService;
    private final MonthlyReportGenerator monthlyReportGenerator;

    @GetMapping
    public String home(HttpSession session,
                       Model model,
                       Authentication authentication,
                       @RequestParam(value = "date", required = false) String date) {
        Date dateObj = DateUtils.getDateFromYearMontInput(date);
        List<WorkDaySingleDto> workDaysModel = monthlyReportGenerator.generateEmployee(dateObj, authentication.getName());
        model.addAttribute("workDays", workDaysModel);
        model.addAttribute("date", DateUtils.formatYearMonth(dateObj));

        session.setAttribute("workDaysResultModel", workDaysModel);
        session.setAttribute("workDaysResultType", WorkDaysResultType.EMPLOYEE);

        return "home";
    }

    @GetMapping("/sign")
    public String signPage(Model model,
                           @RequestParam(value = "date", required = false) String date) {
        if (date == null) date = DateUtils.formatDateWithSeparators(new Date());
        model.addAttribute("date", date);
        return "sign";
    }

    @PostMapping("/sign")
    public String sign(Authentication authentication,
                       @RequestParam("canvasimg") String canvasimg,
                       @RequestParam("date") String date) {
        timeSheetService.save(date, authentication.getName(), canvasimg, WorkType.SIGN);
        return "redirect:/timesheet";
    }

    @GetMapping("/employees")
    public String timeSheetForEmployees(HttpSession session,
                                        Model model,
                                        @RequestParam(value = "date", required = false) String date) {
        Date dateObj = DateUtils.getDateFromYearMontInput(date);

        List<WorkDayDto> workDaysResultModel = monthlyReportGenerator.generateEmployees(dateObj);
        model.addAttribute("workDays", workDaysResultModel);
        model.addAttribute("logins", employeeService.getEmployeesLogins());
        model.addAttribute("date", DateUtils.formatYearMonth(dateObj));

        session.setAttribute("workDaysResultModel", workDaysResultModel);
        session.setAttribute("workDaysResultType", WorkDaysResultType.ALL);

        return "employees";
    }

    @GetMapping("/absence")
    public String reportAbsence(AbsenceDto absenceDto, Model model) {
        model.addAttribute("workTypes", Arrays.stream(WorkType.values()).filter(wt -> wt != WorkType.SIGN).collect(Collectors.toList()));
        return "absence";
    }

    @PostMapping("/absence")
    public String saveAbsence(Authentication authentication,
                              Model model,
                              AbsenceDto absenceDto,
                              BindingResult bindingResult) {

        String err = timeSheetService.validation(absenceDto, authentication.getName());
        if (!err.isEmpty()) {
            bindingResult.addError(new ObjectError("globalError", err));
            model.addAttribute("workTypes", Arrays.stream(WorkType.values()).filter(wt -> wt != WorkType.SIGN).collect(Collectors.toList()));
            return "absence";
        }

        timeSheetService.saveByRange(absenceDto, authentication.getName());

        return "redirect:/timesheet";
    }

    @PostMapping("/save-pdf")
    public void savePdf(HttpServletResponse response,
                        Authentication authentication,
                        HttpSession session) throws IOException {

        WorkDaysResultType workDaysResultType = (WorkDaysResultType) session.getAttribute("workDaysResultType");

        String path = "";
        switch (workDaysResultType) {
            case ALL:
                List<String> logins = employeeService.getEmployees()
                        .stream()
                        .map(Employee::getLogin)
                        .collect(Collectors.toList());
                path = PdfUtils.createForAllEmployees((List<WorkDayDto>) session.getAttribute("workDaysResultModel"), logins, false);
                break;
            case EMPLOYEE:
                path = PdfUtils.createEmployee(authentication.getName(), (List<WorkDaySingleDto>) session.getAttribute("workDaysResultModel"), false);
                break;
        }

        response.setContentType("application/octet-stream");
        String headerKey = "Content-Disposition";
        String headerValue = "attachment; filename = " + Paths.get(path).getFileName();
        response.setHeader(headerKey, headerValue);
        ServletOutputStream outputStream = response.getOutputStream();
        outputStream.write(FileUtils.readFileToByteArray(new File(path)));
        outputStream.close();
    }
}
