package by.bsuir.foodordering.api;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.stream.Stream;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders; // Нужен для Content-Type
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;   // Нужен для Content-Type
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/admin/logs")
public class LogController {

    private static final Logger logger = LoggerFactory.getLogger(LogController.class);

    @Value("${log.file.retrieve.path}")
    private String logFilePath;

    @GetMapping(value = "/view/by", produces = MediaType.TEXT_PLAIN_VALUE)
    public ResponseEntity<String> viewLogsByDate(
            @RequestParam("date") String dateStr) {

        LocalDate requestedDate;
        try {
            requestedDate = LocalDate.parse(dateStr, DateTimeFormatter.ISO_DATE);
        } catch (DateTimeParseException e) {
            logger.warn("Invalid date format received: {}", dateStr);
            return ResponseEntity.badRequest().body("Invalid date format. Please use YYYY-MM-DD.");
        }

        Path path = Paths.get(logFilePath);
        if (!Files.exists(path)) {
            logger.error("Log file not found at path: {}", logFilePath);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Log file not found.");
        }

        List<String> logsForDate;
        try (Stream<String> lines = Files.lines(path, StandardCharsets.UTF_8)) {
            String datePattern = requestedDate.format(DateTimeFormatter.ISO_DATE);

            logsForDate = lines
                    .filter(line -> line.contains(datePattern))
                    .toList();

        } catch (NoSuchFileException e) {
            logger.error("Log file not found during read operation: {}", logFilePath, e);
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body("Log file not found during read.");
        } catch (IOException e) {
            logger.error("Error reading log file: {}", logFilePath, e);
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error reading log file.");
        }

        if (logsForDate.isEmpty()) {
            logger.info("No logs found for date: {}", requestedDate);
            return ResponseEntity.ok().body("No logs found for date: " + requestedDate);
        }

        String logContent = String.join(System.lineSeparator(), logsForDate);

        logger.info("Returning logs view for date: {}", requestedDate);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.TEXT_PLAIN);

        return new ResponseEntity<>(logContent, headers, HttpStatus.OK);
    }
}