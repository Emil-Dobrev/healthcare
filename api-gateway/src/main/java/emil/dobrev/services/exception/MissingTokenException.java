package emil.dobrev.services.exception;

public class MissingTokenException extends RuntimeException{
    public MissingTokenException(String message) {
        super(message);
    }
}
