package pl.raportsa.timesheet.scheduler;

import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import pl.raportsa.timesheet.service.EmployeeService;
import pl.raportsa.timesheet.service.MonthlyReportGenerator;
import pl.raportsa.timesheet.service.TimesheetService;
import pl.raportsa.timesheet.utils.DateUtils;
import pl.raportsa.timesheet.utils.PdfUtils;

import java.util.Date;

@Component
@RequiredArgsConstructor
public class TimesheetTasks {

    private final EmployeeService employeeService;
    private final TimesheetService timeSheetService;

    private final MonthlyReportGenerator monthlyReportGenerator;

    @Scheduled(cron = "0 0 8 1 * *")
    public void generateMonthlyReport() {
        System.err.println("generateMonthlyReport taks");
        Date prevMonth = DateUtils.getPrevMonthDate(new Date());
        generateAll(prevMonth);
        generateEmployees(prevMonth);
    }

    private void generateEmployees(Date prevMonth) {
        employeeService.getEmployeesLogins()
                .forEach(login -> PdfUtils.createEmployee(login, monthlyReportGenerator.generateEmployee(prevMonth, login), true));
    }

    private void generateAll(Date prevMonth) {
        PdfUtils.createForAllEmployees(monthlyReportGenerator.generateEmployees(prevMonth), employeeService.getEmployeesLogins(), true);
    }
}
