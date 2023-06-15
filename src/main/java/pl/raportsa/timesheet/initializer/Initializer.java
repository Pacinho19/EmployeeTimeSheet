package pl.raportsa.timesheet.initializer;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import pl.raportsa.timesheet.model.dto.EmployeeDto;
import pl.raportsa.timesheet.scheduler.TimesheetTasks;
import pl.raportsa.timesheet.service.EmployeeService;

import java.util.List;

@Component
@RequiredArgsConstructor
public class Initializer {

    private final EmployeeService employeeService;

    @EventListener(ApplicationReadyEvent.class)
    public void appReady() {
        if (employeeService.getEmployeesCount() > 0)
            return;

        List.of(
                new EmployeeDto("P", "O", "PO", "test"),
                new EmployeeDto("A", "O", "AO", "test")
        ).forEach(employeeService::register);
    }
}
