package emil.dobrev.services.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.NOT_ACCEPTABLE , reason = "Not valid working day")
public class NotValidWorkingDayException extends RuntimeException{

    public NotValidWorkingDayException(String message){
        super(message);
    }
}
