package pl.raportsa.timesheet.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pl.raportsa.timesheet.model.dto.WorkDayDto;
import pl.raportsa.timesheet.model.dto.WorkDaySingleDto;
import pl.raportsa.timesheet.model.entity.Timesheet;
import pl.raportsa.timesheet.utils.WorkDaysUtils;

import java.util.Date;
import java.util.List;

@Service
@RequiredArgsConstructor
public class MonthlyReportGenerator {

    private final EmployeeService employeeService;
    private final TimesheetService timeSheetService;

    public List<WorkDayDto> generateEmployees(Date date) {
        List<String> logins = employeeService.getEmployeesLogins();
        List<Timesheet> timeSheets = timeSheetService.getTimesheetByEmployeesLogins(logins, date);
        return WorkDaysUtils.getWorkDaysEmployeesModel(logins, timeSheets, date);
    }

    public List<WorkDaySingleDto> generateEmployee(Date date, String login) {
        List<Timesheet> timeSheets = timeSheetService.findByEmployee(login, date);
        return WorkDaysUtils.getWorkDaysEmployeeModel(timeSheets, date);
    }
}
