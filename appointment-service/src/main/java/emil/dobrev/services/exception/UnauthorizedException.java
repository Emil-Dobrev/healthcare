package emil.dobrev.services.exception;

public class UnauthorizedException extends RuntimeException {
        public UnauthorizedException(String message) {
            super(message);
        }

    public UnauthorizedException() {
        super("Not required permissions");
    }
}
