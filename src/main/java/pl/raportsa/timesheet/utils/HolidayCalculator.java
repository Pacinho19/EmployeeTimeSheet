package pl.raportsa.timesheet.utils;

import java.time.LocalDate;
import java.util.Calendar;
import java.util.Date;


public class HolidayCalculator {
    public static boolean isHoliday(LocalDate date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(DateUtils.convert(date));

        if (cal.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY || cal.get(Calendar.DAY_OF_WEEK) == Calendar.SATURDAY) {
            return true; // Weekend
        }
        if (cal.get(Calendar.MONTH) == 0 && cal.get(Calendar.DAY_OF_MONTH) == 1) {
            return true; // Nowy Rok
        }
        if (cal.get(Calendar.MONTH) == 0 && cal.get(Calendar.DAY_OF_MONTH) == 6) {
            return true; // 3 Króli
        }
        if (cal.get(Calendar.MONTH) == 4 && cal.get(Calendar.DAY_OF_MONTH) == 1) {
            return true; // 1 maja
        }
        if (cal.get(Calendar.MONTH) == 4 && cal.get(Calendar.DAY_OF_MONTH) == 3) {
            return true; // 3 maja
        }
        if (cal.get(Calendar.MONTH) == 7 && cal.get(Calendar.DAY_OF_MONTH) == 15) {
            return true; // Wniebowzięcie Najświętszej Marii Panny, Święto Wojska Polskiego
        }
        if (cal.get(Calendar.MONTH) == 10 && cal.get(Calendar.DAY_OF_MONTH) == 1) {
            return true; // Dzień Wszystkich Świętych
        }
        if (cal.get(Calendar.MONTH) == 10 && cal.get(Calendar.DAY_OF_MONTH) == 11) {
            return true; // Dzień Niepodległości
        }
        if (cal.get(Calendar.MONTH) == 11 && cal.get(Calendar.DAY_OF_MONTH) == 25) {
            return true; // Boże Narodzenie
        }
        if (cal.get(Calendar.MONTH) == 11 && cal.get(Calendar.DAY_OF_MONTH) == 26) {
            return true; // Boże Narodzenie
        }
        int a = cal.get((Calendar.YEAR)) % 19;
        int b = cal.get((Calendar.YEAR)) % 4;
        int c = cal.get((Calendar.YEAR)) % 7;
        int d = (a * 19 + 24) % 30;
        int e = (2 * b + 4 * c + 6 * d + 5) % 7;
        if (d == 29 && e == 6) {
            d -= 7;
        }
        if (d == 28 && e == 6 && a > 10) {
            d -= 7;
        }
        Date easter = new Date(cal.get((Calendar.YEAR)) - 1900, 2, 22);
        easter = addDays(easter, d + e);
        if (addDays(cal.getTime(), -1).getTime() == easter.getTime()) {
            return true; // Wielkanoc (poniedziałek)
        }
        if (addDays(cal.getTime(), -60).getTime() == easter.getTime()) {
            return true; // Boże Ciało
        }
        return false;
    }

    private static Date addDays(Date date, int days) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.DATE, days);
        return cal.getTime();
    }

}
