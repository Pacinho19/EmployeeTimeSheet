package pl.raportsa.timesheet.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pl.raportsa.timesheet.model.dto.AbsenceDto;
import pl.raportsa.timesheet.model.entity.Employee;
import pl.raportsa.timesheet.model.entity.Timesheet;
import pl.raportsa.timesheet.model.enums.WorkType;
import pl.raportsa.timesheet.repository.TimesheetRepository;
import pl.raportsa.timesheet.utils.*;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TimesheetService {

    private final TimesheetRepository timeSheetRepository;
    private final EmployeeService employeeService;

    public void save(String date, String login, String signFile, WorkType type) {
        Timesheet timeSheet = new Timesheet();
        timeSheet.setWorkDate(DateUtils.parseDate(date));

        if (!DateUtils.isBeforeOrEqualNow(timeSheet.getWorkDate())) return;

        Employee emp = employeeService.getByLogin(login);
        if (timeSheetRepository.existsByWorkDateAndEmployee(timeSheet.getWorkDate(), emp)) return;

        if (type == WorkType.SIGN) timeSheet.setSignSrc(FileUtils.saveImage(login, signFile));

        timeSheet.setWorkType(type);
        timeSheet.setEmployee(emp);
        timeSheet.setSignDate(new Timestamp(new Date().getTime()));
        timeSheet.setSignHashCode(ChecksumUtils.hash(ImageUtils.encodeFileToBase64Binary(timeSheet.getSignSrc()), timeSheet.getSignDate()));
        timeSheetRepository.save(timeSheet);
    }

    public List<Timesheet> findByEmployee(String name, Date date) {
        return timeSheetRepository.findAllByEmployeeLoginInAndDates(List.of(name), new java.sql.Date(date.getTime()));
    }

    public List<Timesheet> getTimesheetByEmployeesLogins(List<String> logins, Date dateObj) {
        return timeSheetRepository.findAllByEmployeeLoginInAndDates(logins,dateObj);
    }

    public boolean checkTodaySign(Employee employee) {
        return timeSheetRepository.existsByWorkDateAndEmployee(LocalDate.now(), employee);
    }

    public String validation(AbsenceDto absenceDto, String login) {

        if (absenceDto == null) return "Absence details is null !";

        String workType = absenceDto.getWorkType();
        String startDateS = absenceDto.getDateFrom();
        String endDateS = absenceDto.getDateTo();

        if (workType == null || workType.isEmpty()) return "Absence type is empty !";

        try {
            WorkType.valueOf(workType);
        } catch (Exception e) {
            return "Absence type is invalid";
        }

        if (DateUtils.checkEmpty(startDateS)) return "Start date is empty !";
        if (DateUtils.checkEmpty(endDateS)) return "End date is empty !";

        LocalDate startDate = DateUtils.parseDate(startDateS);
        if (startDate == null) return "Start date has invalid format !";

        LocalDate endDate = DateUtils.parseDate(endDateS);
        if (endDate == null) return "End date has invalid format !";

        if (endDate.isBefore(startDate)) return "End date is before Start date !";

        List<java.sql.Date> datesWithExistRecord = timeSheetRepository.findAllByWorkDateBetween(startDate, endDate, login);
        if (!datesWithExistRecord.isEmpty())
            return datesWithExistRecord.stream().map(DateUtils::formatWithSeparators).collect(Collectors.joining("\n", "Work days in dates : ", " are already completed. Select dates without sign, L4 or vacation."));

        return "";
    }

    public void saveByRange(AbsenceDto absenceDto, String login) {
        Employee e = employeeService.getByLogin(login);
        LocalDate startDate = DateUtils.parseDate(absenceDto.getDateFrom());
        LocalDate endDate = DateUtils.parseDate(absenceDto.getDateTo());

        Calendar c = Calendar.getInstance();
        c.setTime(DateUtils.convert(startDate));

        Calendar c2 = Calendar.getInstance();
        c2.setTime(DateUtils.convert(endDate));

        while (c.getTime().before(c2.getTime()) || c.getTime().equals(c2.getTime())) {
            if (HolidayCalculator.isHoliday(DateUtils.convert(c.getTime()))) {
                c.add(Calendar.DATE, 1);
                continue;
            }

            Timesheet t = new Timesheet();
            t.setEmployee(e);
            t.setWorkDate(DateUtils.convert(c.getTime()));
            t.setWorkType(WorkType.valueOf(absenceDto.getWorkType()));
            t.setSignDate(new Timestamp(new Date().getTime()));
            timeSheetRepository.save(t);

            c.add(Calendar.DATE, 1);
        }

    }
}
