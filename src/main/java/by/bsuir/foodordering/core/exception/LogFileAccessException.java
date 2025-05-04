package by.bsuir.foodordering.core.exception;

public class LogFileAccessException extends RuntimeException {

    public LogFileAccessException(String message) {
        super(message);
    }

    public LogFileAccessException(String message, Throwable cause) {
        super(message, cause);
    }
}