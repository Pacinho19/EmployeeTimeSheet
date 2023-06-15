package pl.raportsa.timesheet.model.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import pl.raportsa.timesheet.model.enums.WorkType;

@Getter
@Setter
@AllArgsConstructor
public class Work {
    private String signSrc;
    private String signBase64;
    private WorkType workType;
    private boolean signError;
}
