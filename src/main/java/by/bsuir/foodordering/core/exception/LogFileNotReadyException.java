package by.bsuir.foodordering.core.exception;

public class LogFileNotReadyException extends RuntimeException {
    public LogFileNotReadyException(String status) {
        super("Log file is not ready yet. Status: " + status);
    }
}