package pl.raportsa.timesheet.model.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class WorkDaySingleDto extends WorkDay{
    private Work work;
    private boolean canSign;
}
