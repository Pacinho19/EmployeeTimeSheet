package pl.raportsa.timesheet.model.enums.email;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum EmailTitle {

    SIGN_VERIFICATION("Signature verification");

    private final String title;
}
