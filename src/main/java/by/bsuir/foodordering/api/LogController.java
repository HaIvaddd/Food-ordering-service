package by.bsuir.foodordering.api;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.io.ByteArrayInputStream;
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
import java.util.Map;
import java.util.stream.Stream;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/admin/logs")
@Tag(name = "Admin Log Management",
        description = "Endpoints for accessing and viewing application logs")
public class LogController {

    private static final Logger logger = LoggerFactory.getLogger(LogController.class);

    @Value("${log.file.retrieve.path}")
    private String logFilePath;

    @Operation(
            summary = "Download log entries by date",
            description = "Retrieves log entries from the application log file for "
                    + "a specified date and returns them as a downloadable plain text file. "
                    + "Filtering is based on finding the date string (YYYY-MM-DD) within log lines."
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200",
                description = "Log file generated successfully for download",
                content = @Content(mediaType = MediaType.TEXT_PLAIN_VALUE,
                        schema = @Schema(type = "string",
                                format = "binary",
                                description = "Log file content"))),
        @ApiResponse(responseCode = "400", description = "Invalid date format provided",
                // Ошибка все еще может вернуть JSON или текст с описанием
                content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                        schema = @Schema(implementation = Map.class))),
        @ApiResponse(responseCode = "404",
                description = "Log file not found or no logs found for the specified date",
                content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                        schema = @Schema(implementation = Map.class))),
        @ApiResponse(responseCode = "500", description = "Error reading the log file",
                content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                        schema = @Schema(implementation = Map.class)))
    })

    @GetMapping(value = "/download/by-date", produces = MediaType.TEXT_PLAIN_VALUE)
    public ResponseEntity<Resource> downloadLogsByDate(
            @Parameter(
                    description = "Date to filter logs by (format: YYYY-MM-DD)",
                    required = true,
                    example = "2024-03-28",
                    schema = @Schema(type = "string", format = "date")
            )
            @RequestParam("date") String dateStr) {

        LocalDate requestedDate;
        try {
            requestedDate = LocalDate.parse(dateStr, DateTimeFormatter.ISO_DATE);
        } catch (DateTimeParseException e) {
            logger.warn("Invalid date format received: {}", dateStr);
            return ResponseEntity.badRequest().body(null);
        }

        Path path = Paths.get(logFilePath);
        if (!Files.exists(path)) {
            logger.error("Log file not found at path: {}", logFilePath);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }

        List<String> logsForDate;
        try (Stream<String> lines = Files.lines(path, StandardCharsets.UTF_8)) {
            String datePattern = requestedDate.format(DateTimeFormatter.ISO_DATE);
            logsForDate = lines
                    .filter(line -> line.contains(datePattern))
                    .toList();
        } catch (NoSuchFileException e) {
            logger.error("Log file not found during read operation: {}", logFilePath, e);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        } catch (IOException e) {
            logger.error("Error reading log file: {}", logFilePath, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }

        if (logsForDate.isEmpty()) {
            logger.info("No logs found for date: {}", requestedDate);
            return ResponseEntity.notFound().build();
        }

        String logContent = String.join(System.lineSeparator(), logsForDate);

        ByteArrayInputStream inputStream = new ByteArrayInputStream(logContent
                                                    .getBytes(StandardCharsets.UTF_8));

        String filename = "logs-" + dateStr + ".log";

        logger.info("Preparing log file download for date: {}", requestedDate);

        InputStreamResource resource = new InputStreamResource(inputStream);

        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + filename + "\"");
        headers.setContentType(MediaType.TEXT_PLAIN);

        return new ResponseEntity<>(resource, headers, HttpStatus.OK);
    }
}