package pl.raportsa.timesheet.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import pl.raportsa.timesheet.model.entity.Employee;
import pl.raportsa.timesheet.model.entity.Timesheet;
import pl.raportsa.timesheet.repository.queries.NativeQueries;

import java.time.LocalDate;
import java.sql.Date;
import java.util.List;

@Repository
public interface TimesheetRepository extends JpaRepository<Timesheet, Long> {

    boolean existsByWorkDateAndEmployee(LocalDate workDate, Employee emp);

    List<Timesheet> findAllByEmployeeLoginIn(List<String> logins);

    @Query(value = NativeQueries.SELECT_EXIST_TIMESHEET_WORK_DATE_BETWEEN_DATES
            , nativeQuery = true)
    List<Date> findAllByWorkDateBetween(@Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate, @Param("login") String login);

    @Query(value = NativeQueries.SELECT_TIMESHEET_BY_EMPLOYEES_AND_DATE
            , nativeQuery = true)
    List<Timesheet> findAllByEmployeeLoginInAndDates(@Param("logins") List<String> logins, @Param("date") java.util.Date dateObj);
}
