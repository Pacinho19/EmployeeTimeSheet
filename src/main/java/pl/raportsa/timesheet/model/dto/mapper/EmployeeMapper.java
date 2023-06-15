package pl.raportsa.timesheet.model.dto.mapper;

import pl.raportsa.timesheet.model.dto.EmployeeDto;
import pl.raportsa.timesheet.model.entity.Employee;

public class EmployeeMapper {
    public static Employee parseFromDto(EmployeeDto employee) {
        return Employee.builder()
                .firstName(employee.firstName())
                .lastName(employee.lastName())
                .login(employee.login())
                .build();
    }
}
