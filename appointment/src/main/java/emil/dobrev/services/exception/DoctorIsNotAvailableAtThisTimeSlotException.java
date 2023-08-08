package emil.dobrev.services.exception;

public class DoctorIsNotAvailableAtThisTimeSlotException extends RuntimeException {
    public DoctorIsNotAvailableAtThisTimeSlotException(String message) {
        super(message);
    }
}
