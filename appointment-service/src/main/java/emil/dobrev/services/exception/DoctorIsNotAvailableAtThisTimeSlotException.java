package emil.dobrev.services.exception;

import java.time.LocalDateTime;

public class DoctorIsNotAvailableAtThisTimeSlotException extends RuntimeException {
    public DoctorIsNotAvailableAtThisTimeSlotException(Long doctorId, LocalDateTime requestedDateTime) {
        super(String.format("Doctor with ID %d is not available at %s.", doctorId, requestedDateTime));
    }

    public DoctorIsNotAvailableAtThisTimeSlotException(String message) {
        super(message);
    }
}
