package emil.dobrev.services.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;

import static emil.dobrev.services.constant.Constants.UNAUTHORIZED;

public class GlobalExceptionHandler {

    @ExceptionHandler(UnauthorizedException.class)
    public ResponseEntity<ErrorResponse> handleUnauthorizedException(UnauthorizedException ex) {
        var errorResponse = new ErrorResponse(
                ex.getMessage(),
                HttpStatus.UNAUTHORIZED.value(),
                UNAUTHORIZED);
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(errorResponse);
    }

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<ErrorResponse> handleHolidaysNotFoundException(NotFoundException ex) {
        var errorResponse = new ErrorResponse
                (
                        ex.getMessage(),
                        HttpStatus.NOT_FOUND.value(),
                        HttpStatus.NOT_FOUND.getReasonPhrase()
                );
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
    }

    @ExceptionHandler(DoctorIsNotAvailableAtThisTimeSlotException.class)
    public ResponseEntity<ErrorResponse> handleDoctorIsNotAvailableAtThisTimeSlotException(
            DoctorIsNotAvailableAtThisTimeSlotException ex
    ) {
        var errorResponse = new ErrorResponse
                (
                        ex.getMessage(),
                        HttpStatus.CONFLICT.value(),
                        HttpStatus.CONFLICT.getReasonPhrase()
                );
        return ResponseEntity.status(HttpStatus.CONFLICT).body(errorResponse);
    }
}
