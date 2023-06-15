package pl.raportsa.timesheet.model.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public abstract class WorkDay {

    private LocalDate date;
    private String weekdayName;
    private boolean holiday;
}
