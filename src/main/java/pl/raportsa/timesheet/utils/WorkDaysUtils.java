package pl.raportsa.timesheet.utils;

import pl.raportsa.timesheet.model.dto.Work;
import pl.raportsa.timesheet.model.dto.WorkDay;
import pl.raportsa.timesheet.model.dto.WorkDayDto;
import pl.raportsa.timesheet.model.dto.WorkDaySingleDto;
import pl.raportsa.timesheet.model.entity.Timesheet;

import java.time.LocalDate;
import java.util.*;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

public class WorkDaysUtils {


    public static List<WorkDaySingleDto> getWorkDaysEmployeeModel(List<Timesheet> timeSheets, Date dateObj) {
        List<WorkDaySingleDto> workDays = new ArrayList<>();

        Map<LocalDate, Timesheet> timeSheetMap = timeSheets.stream()
                .collect(Collectors.toMap(Timesheet::getWorkDate, Function.identity()));

        TreeSet<LocalDate> listOfDaysCurrentMonth = DateUtils.getListDaysOfMonth(dateObj);
        listOfDaysCurrentMonth.forEach(date -> {
            WorkDaySingleDto workDay = new WorkDaySingleDto();
            setWorkDayParams(workDay, date);
            workDay.setCanSign(DateUtils.isBeforeOrEqualNow(date) && DateUtils.isCurrentMont(date));

            Timesheet timesheet = timeSheetMap.get(date);
            if (timesheet != null) {
                String imageBase64 = null;
                boolean signError = false;
                if (timesheet.getSignSrc() != null) {
                    imageBase64 = ImageUtils.encodeFileToBase64Binary(timesheet.getSignSrc());
                    signError = imageBase64 == null || !ChecksumUtils.isEquals(ChecksumUtils.hash(imageBase64, timesheet.getSignDate()), timesheet.getSignHashCode());
                    //throw new SecurityException(ErrorMessage.SECURITY.getMessage());

                }
                workDay.setWork(new Work(timesheet.getSignSrc(), imageBase64, timesheet.getWorkType(), signError));
            }
            workDays.add(workDay);
        });
        return workDays;
    }

    public static List<WorkDayDto> getWorkDaysEmployeesModel(List<String> logins, List<Timesheet> timeSheets, Date dateObj) {
        List<WorkDayDto> workDays = new ArrayList<>();

        Map<LocalDate, List<Timesheet>> timeSheetMap = timeSheets.stream()
                .collect(Collectors.groupingBy(Timesheet::getWorkDate));

        TreeSet<LocalDate> listOfDaysCurrentMonth = DateUtils.getListDaysOfMonth(dateObj);

        listOfDaysCurrentMonth.forEach(date -> {
            WorkDayDto workDay = new WorkDayDto();
            workDays.add(workDay);

            setWorkDayParams(workDay, date);
            List<Timesheet> timeSheetsByDate = timeSheetMap.get(date);

            if (timeSheetsByDate == null) return;

            Map<String, Timesheet> byEmp = timeSheetsByDate.stream()
                    .collect(Collectors.toMap(t -> t.getEmployee().getLogin(), Function.identity()));

            logins.forEach(l -> {
                Timesheet timesheet = byEmp.get(l);

                if (timesheet == null) return;

                String imageBase64 = null;
                boolean signError = false;
                if (timesheet.getSignSrc() != null) {
                    imageBase64 = ImageUtils.encodeFileToBase64Binary(timesheet.getSignSrc());
                    signError = imageBase64 == null || !ChecksumUtils.isEquals(ChecksumUtils.hash(imageBase64, timesheet.getSignDate()), timesheet.getSignHashCode());
                    //throw new SecurityException(ErrorMessage.SECURITY.getMessage());
                }

                workDay.addWork(l, new Work(timesheet.getSignSrc(), imageBase64, timesheet.getWorkType(), signError));
            });
        });
        return workDays;
    }

    private static void setWorkDayParams(WorkDay workDay, LocalDate date) {
        workDay.setDate(date);
        workDay.setWeekdayName(DateUtils.getWeekDayName(date).toUpperCase());
        workDay.setHoliday(HolidayCalculator.isHoliday(date));
    }
}
