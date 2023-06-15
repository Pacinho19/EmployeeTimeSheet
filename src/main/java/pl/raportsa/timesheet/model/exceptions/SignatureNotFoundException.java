package pl.raportsa.timesheet.model.exceptions;

public class SignatureNotFoundException extends RuntimeException {
    public SignatureNotFoundException(String message) {
        super(message);
    }
}
