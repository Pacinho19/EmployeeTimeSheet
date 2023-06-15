package pl.raportsa.timesheet.model.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AbsenceDto {

    private String dateFrom;
    private String dateTo;
    private String workType;
}
