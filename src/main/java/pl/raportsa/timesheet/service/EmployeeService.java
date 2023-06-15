package pl.raportsa.timesheet.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import pl.raportsa.timesheet.config.CryptoConfig;
import pl.raportsa.timesheet.model.dto.EmployeeDto;
import pl.raportsa.timesheet.model.entity.Employee;
import pl.raportsa.timesheet.model.dto.mapper.EmployeeMapper;
import pl.raportsa.timesheet.repository.EmployeeRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class EmployeeService implements UserDetailsService {

    private final EmployeeRepository employeeRepository;
    private final CryptoConfig cryptoConfig;

    @Override
    public UserDetails loadUserByUsername(String login) throws UsernameNotFoundException {
        return employeeRepository.findByLogin(login)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
    }

    public Employee getByLogin(String login) {
        return employeeRepository.findByLogin(login)
                .get();
    }

    public Employee register(EmployeeDto employeeDto) {
        Employee employee = EmployeeMapper.parseFromDto(employeeDto);
        employee.setPassword(cryptoConfig.encoder().encode(employeeDto.pass()));
        return employeeRepository.save(employee);
    }

    public long getEmployeesCount() {
        return employeeRepository.count();
    }

    public List<Employee> getEmployees() {
        return employeeRepository.findAll();
    }

    public List<String> getEmployeesLogins() {
        return getEmployees().stream()
                .map(Employee::getLogin)
                .toList();
    }
}
