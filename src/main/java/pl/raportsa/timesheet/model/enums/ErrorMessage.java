package pl.raportsa.timesheet.model.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
public enum ErrorMessage {

    SECURITY("Checksum of any sign is invalid !!\n" +
            "Our special services have already been informed and are starting to intervene."),
     SIGN_NOT_FOUND("Any sign src not found");
    @Getter
    private String message;
}
