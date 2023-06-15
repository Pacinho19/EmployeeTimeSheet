package pl.raportsa.timesheet.utils;

import com.itextpdf.text.pdf.PdfPCell;

import java.text.DateFormatSymbols;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.*;
import java.util.Calendar;
import java.util.Date;
import java.util.TreeSet;

public class DateUtils {

    private static final SimpleDateFormat sdfDate = new SimpleDateFormat("yyyyMMdd");
    private static final SimpleDateFormat sdfDate2 = new SimpleDateFormat("yyyy-MM-dd");
    private static final SimpleDateFormat sdfDateYearMonth = new SimpleDateFormat("yyyy-MM");
    private static final SimpleDateFormat sdfDateYearMonthWithoutSeparators = new SimpleDateFormat("yyyyMM");
    private static final SimpleDateFormat sdfDateYearMonthWitSlashSeparator = new SimpleDateFormat("yyyy/MM");

    private static final String[] weekDaysNames = new DateFormatSymbols().getShortWeekdays();

    public static String formatYearMonth(Date dateObj) {
        return sdfDateYearMonth.format(dateObj);
    }

    public static String formatDateWithSeparators(Date date) {
        return sdfDate2.format(date);
    }

    public static String formatWithoutSeparators(Date date) {
        return sdfDate.format(date);
    }

    public static String formatWithSeparators(java.sql.Date date) {
        return sdfDate2.format(date);
    }

    public static String formatWithSeparators(LocalDate localDate) {
        return sdfDate2.format(convert(localDate));
    }

    public static String formatWithoutSeparators(LocalDate date) {
        return sdfDate.format(convert(date));
    }

    public static String formatYearMonthWithoutSeparators(LocalDate date) {
        return sdfDateYearMonthWithoutSeparators.format(convert(date));
    }

    public static String formatYearMonthWithSlashSeparator(LocalDate date) {
        return sdfDateYearMonthWitSlashSeparator.format(convert(date));
    }

    public static LocalDate convert(Date dateToConvert) {
        return dateToConvert.toInstant()
                .atZone(ZoneId.systemDefault())
                .toLocalDate();
    }

    public static Date convert(LocalDate dateToConvert) {
        return java.util.Date.from(dateToConvert.atStartOfDay()
                .atZone(ZoneId.systemDefault())
                .toInstant());
    }

    public static LocalDate parseDate(String date) {
        try {
            return convert(sdfDate2.parse(date));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static Date parseYearMonth(String date) {
        try {
            return sdfDateYearMonth.parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static Date getDateFromYearMontInput(String date) {
        Date dateObj = date == null ? new Date() : DateUtils.parseYearMonth(date);
        if (dateObj == null) dateObj = new Date();
        return dateObj;
    }

    public static TreeSet<LocalDate> getListDaysOfMonth(Date dateObj) {
        TreeSet<LocalDate> out = new TreeSet<>();

        Calendar c = Calendar.getInstance();
        c.setTime(dateObj);
        c.set(Calendar.DATE, 1);
        c.set(Calendar.HOUR, 0);
        c.set(Calendar.MINUTE, 0);
        c.set(Calendar.SECOND, 0);
        c.set(Calendar.MILLISECOND, 0);

        Calendar c2 = Calendar.getInstance();
        c2.setTime(c.getTime());
        c2.set(Calendar.DATE, 1);
        c2.add(Calendar.MONTH, 1);

        while (c.getTime().before(c2.getTime())) {
            out.add(DateUtils.convert(c.getTime()));
            c.add(Calendar.DATE, 1);
        }
        return out;
    }

    public static String getWeekDayName(LocalDate localDate) {
        Calendar c = Calendar.getInstance();
        c.setTime(convert(localDate));
        return weekDaysNames[c.get(Calendar.DAY_OF_WEEK)];
    }


    public static boolean isBeforeOrEqualNow(LocalDate date) {
        LocalDate today = LocalDate.now();
        return date.isBefore(today) || date.isEqual(today);
    }

    public static boolean checkEmpty(String date) {
        return date == null || date.isEmpty();
    }

    public static boolean isCurrentMont(LocalDate date) {
        LocalDate now = LocalDate.now();
        return now.getMonth() == date.getMonth()
                && now.getYear() == date.getYear();
    }

    public static Date getPrevMonthDate(Date date) {
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        c.set(Calendar.MONTH, c.get(Calendar.MONTH)-1);
        return c.getTime();
    }
}
