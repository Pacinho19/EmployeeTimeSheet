package pl.raportsa.timesheet.model.enums.email;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum EmailType {

    SIGN_VERIFICATION(EmailTitle.SIGN_VERIFICATION, EmailText.SIGN_VERIFICATION);

    private final EmailTitle emailTitle;
    private final EmailText emailText;
}
