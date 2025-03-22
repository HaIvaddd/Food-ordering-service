package by.bsuir.foodordering.core.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class MakeOrderException extends RuntimeException {
    public MakeOrderException(String message) {
        super(message);
    }
}
