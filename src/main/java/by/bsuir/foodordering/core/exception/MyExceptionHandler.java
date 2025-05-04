package by.bsuir.foodordering.core.exception;

import java.util.HashMap;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.server.ResponseStatusException;

@ControllerAdvice
public class MyExceptionHandler {

    private static final String EXC = "Exception: ";
    private static final String MSG = "Message: ";

    private static final Logger logger = LoggerFactory.getLogger(MyExceptionHandler.class);

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public ResponseEntity<Map<String, String>>
        handleValidationExceptions(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getFieldErrors().forEach(error -> {
            errors.put(error.getField(), error.getDefaultMessage());
            logger.warn("Validation error in field '{}': {}",
                    error.getField(), error.getDefaultMessage());
        });
        return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler({UserTypeException.class, FoodTypeException.class})
    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ResponseBody
    public ResponseEntity<Map<String, String>>
        handleTypeExceptions(Exception ex) {
        Map<String, String> errors = new HashMap<>();
        errors.put(EXC,  "Type not found");
        errors.put(MSG,  ex.getMessage());
        logger.warn("Type error in field '{}': {}", ex.getClass().getName(), ex.getMessage());
        return new ResponseEntity<>(errors, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(CreatedEntityException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public ResponseEntity<Map<String, String>>
        handleCreatedEntityExceptions(CreatedEntityException ex) {
        Map<String, String> errors = new HashMap<>();
        errors.put(EXC,  "Created entity");
        errors.put(MSG,  ex.getMessage());
        logger.warn("Created entity error in field '{}': {}",
                ex.getClass().getName(), ex.getMessage());
        return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(EntityNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ResponseBody
    public ResponseEntity<Map<String, String>>
        handleEntityNotFoundExceptions(EntityNotFoundException ex) {
        Map<String, String> errors = new HashMap<>();
        errors.put(EXC,  "Entity not found");
        errors.put(MSG,  ex.getMessage());
        logger.warn("Entity not found in field '{}': {}",
                ex.getClass().getName(), ex.getMessage());
        return new ResponseEntity<>(errors, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(MakeOrderException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public ResponseEntity<Map<String, String>>
        handleEntityNotFoundExceptions(MakeOrderException ex) {
        Map<String, String> errors = new HashMap<>();
        errors.put(EXC,  "Make Order");
        errors.put(MSG,  ex.getMessage());
        logger.warn("Make Order error in field '{}': {}",
                ex.getClass().getName(), ex.getMessage());
        return ResponseEntity.badRequest().body(errors);
    }

    @ExceptionHandler(DateException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public ResponseEntity<Map<String, String>>
        handleDateExceptions(DateException ex) {
        Map<String, String> errors = new HashMap<>();
        errors.put(EXC,  "Date");
        errors.put(MSG,  ex.getMessage());
        logger.warn("Date log '{}': {}",
                ex.getClass().getName(), ex.getMessage());
        return ResponseEntity.badRequest().body(errors);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public ResponseEntity<Map<String, String>>
        handleArgumentExceptions(IllegalArgumentException ex) {
        Map<String, String> errors = new HashMap<>();
        errors.put(EXC,  "Argument exception");
        errors.put(MSG,  ex.getMessage());
        logger.warn("Argument error in field '{}': {}", ex.getClass().getName(), ex.getMessage());
        return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
    }


    @ExceptionHandler(TaskNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ResponseBody
    public ResponseEntity<Map<String, String>> handleTaskNotFound(TaskNotFoundException ex) {
        Map<String, String> errors = new HashMap<>();
        errors.put(EXC, "TASK_NOT_FOUND");
        errors.put(MSG, ex.getMessage());
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        return new ResponseEntity<>(errors, headers, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(LogFileNotReadyException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    @ResponseBody
    public ResponseEntity<Map<String, String>> handleLogNotReady(LogFileNotReadyException ex) {
        Map<String, String> errorDetails = new HashMap<>();
        errorDetails.put(EXC, "LOG_FILE_NOT_READY");
        errorDetails.put(MSG, ex.getMessage());
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        return new ResponseEntity<>(errorDetails, headers, HttpStatus.CONFLICT);
    }


    @ExceptionHandler(LogFileAccessException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ResponseBody
    public ResponseEntity<Map<String, String>> handleFileAccess(LogFileAccessException ex) {
        Map<String, String> errorDetails = new HashMap<>();
        errorDetails.put(EXC, "LOG_FILE_ACCESS_ERROR");
        errorDetails.put(MSG, "An internal error occurred while accessing the log file.");
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        return new ResponseEntity<>(errorDetails, headers, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
