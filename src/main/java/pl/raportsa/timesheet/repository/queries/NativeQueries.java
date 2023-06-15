package pl.raportsa.timesheet.repository.queries;

import pl.raportsa.timesheet.model.entity.Timesheet;

public class NativeQueries {
    public static final String SELECT_EXIST_TIMESHEET_WORK_DATE_BETWEEN_DATES = "SELECT T.WORK_DATE\n" +
            "from " + Timesheet.TABLE_NAME + " T\n" +
            "join employees p on p.id=t.EMPLOYEE_ID\n" +
            "where T.WORK_DATE BETWEEN :startDate and :endDate\n\n" +
            "and p.login=:login";

    public static final String SELECT_TIMESHEET_BY_EMPLOYEES_AND_DATE = "select ts.*\n" +
            "from " + Timesheet.TABLE_NAME + " ts\n" +
            "join employees p on p.id=ts.EMPLOYEE_ID\n" +
            "where EXTRACT(MONTH from ts.WORK_DATE)=EXTRACT(MONTH from cast(:date as date))\n" +
            "and EXTRACT(YEAR from ts.WORK_DATE)=EXTRACT(YEAR from cast(:date as date))\n" +
            "and p.login in :logins";
}
