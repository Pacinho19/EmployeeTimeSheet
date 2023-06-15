package pl.raportsa.timesheet.model.enums.email;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum EmailText {

    SIGN_VERIFICATION("You not signed today yet.\nPlease login into employee timesheet and sign it!");

    private final String text;
}