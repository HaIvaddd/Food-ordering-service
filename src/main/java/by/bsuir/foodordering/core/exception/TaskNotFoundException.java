package by.bsuir.foodordering.core.exception;

public class TaskNotFoundException extends RuntimeException {
    public TaskNotFoundException(String taskId) {
        super("Task not found with ID: " + taskId);
    }
}