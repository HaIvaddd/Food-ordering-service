package by.bsuir.foodordering.api;

import by.bsuir.foodordering.core.service.impl.AsyncLogService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.format.DateTimeParseException;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@RestController
@AllArgsConstructor
@RequestMapping("/api/log-tasks")
@Tag(name = "Async Application Log Task Management",
        description = "Endpoints for asynchronously filtering application logs by date.")
public class AsyncLogController { // Переименовали для ясности

    private static final Logger logger = LoggerFactory.getLogger(AsyncLogController.class);
    private final AsyncLogService asyncLogService;

    @Operation(
            summary = "Initiate asynchronous filtering of application logs by date",
            description = "Starts a background task to filter the main application log file "
                    + "for a specified date (YYYY-MM-DD). Returns a task ID for status checking."
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "202", description = "Log filtering task accepted",
                content = @Content(mediaType = MediaType.TEXT_PLAIN_VALUE,
                        schema = @Schema(type = "string",
                                example = "f47ac10b-58cc-4372-a567-0e02b2c3d479"))),
        @ApiResponse(responseCode = "400",
                description = "Invalid date format provided (expected YYYY-MM-DD)",
                content = @Content(mediaType = MediaType.TEXT_PLAIN_VALUE)),
        @ApiResponse(responseCode = "500", description = "Failed to initiate the task",
                content = @Content(mediaType = MediaType.TEXT_PLAIN_VALUE))
    })
    @PostMapping("/filter-by-date")
    public ResponseEntity<String> filterLogByDate(
            @Parameter(
                    description = "Date to filter logs by (format: YYYY-MM-DD)",
                    required = true,
                    example = "2024-04-08",
                    schema = @Schema(type = "string", format = "date")
            )
            @RequestParam("date") String date) {
        logger.info("Received request to filter logs for date: {}", date);
        if (date == null || date.isEmpty()) {
            return ResponseEntity.badRequest().body("Date parameter 'date' is required.");
        }
        try {
            String taskId = asyncLogService.filterAppLogByDateAsync(date);
            logger.info("Log filtering task initiated with ID: {}", taskId);
            return ResponseEntity.accepted().body(taskId);
        } catch (DateTimeParseException e) {
            logger.warn("Invalid date format received: {}", date, e);
            return ResponseEntity.badRequest()
                    .body("Invalid date format for parameter 'date'. Expected YYYY-MM-DD.");
        } catch (Exception e) {
            logger.error("Failed to initiate log filtering task for date {}", date, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Failed to initiate log filtering task: " + e.getMessage());
        }
    }

    @Operation(summary = "Get status of a log filtering task",
            description = "Retrieves the current status of an asynchronous "
                    + "log filtering task by its ID.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Status retrieved successfully",
                content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                        schema = @Schema(implementation = AsyncLogService.LogTaskStatus.class))),
        @ApiResponse(responseCode = "404",
                description = "Task not found for the given ID", content = @Content)
    })
    @GetMapping("/{taskId}/status")
    public ResponseEntity<AsyncLogService.LogTaskStatus> getLogStatus(
            @Parameter(description = "ID of the task to check",
                    required = true, example = "f47ac10b-58cc-4372-a567-0e02b2c3d479")
            @PathVariable String taskId) {
        logger.debug("Request received for status of task ID: {}", taskId);
        AsyncLogService.LogTaskStatus status = asyncLogService.getTaskStatus(taskId);
        if (status == null) {
            logger.warn("Status requested for non-existent task ID: {}", taskId);
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND,
                    "Task not found with ID: " + taskId);
        }
        logger.debug("Returning status for task ID {}: {}", taskId, status.getStatus());
        return ResponseEntity.ok(status);
    }

    @Operation(
            summary = "Download the filtered application log file",
            description = "Downloads the log file generated "
                    + "by a completed asynchronous filtering task."
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Log file download started",
                content = @Content(mediaType = MediaType.TEXT_PLAIN_VALUE,
                        schema = @Schema(type = "string", format = "binary"))),
        @ApiResponse(responseCode = "404",
                description = "Task not found or associated file not found", content = @Content),
        @ApiResponse(responseCode = "409",
                description = "Log file is not ready yet (task not completed or failed)",
                content = @Content),
        @ApiResponse(responseCode = "500",
                description = "Error accessing the log file", content = @Content)
    })
    @GetMapping(value = "/{taskId}/download", produces = MediaType.TEXT_PLAIN_VALUE)
    public ResponseEntity<Resource> downloadLogFile(
            @Parameter(description = "ID of the task whose log file to download",
                    required = true, example = "f47ac10b-58cc-4372-a567-0e02b2c3d479")
            @PathVariable String taskId) {
        logger.info("Request received to download log file for task ID: {}", taskId);
        AsyncLogService.LogTaskStatus status = asyncLogService.getTaskStatus(taskId);
        if (status == null) {
            logger.warn("Download requested for non-existent task ID: {}", taskId);
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND,
                    "Task not found with ID: " + taskId);
        }

        if (status.getStatus() != AsyncLogService.Status.COMPLETED) {
            logger.warn("Download requested for task ID {} but status is: {}",
                    taskId, status.getStatus());
            throw new ResponseStatusException(HttpStatus.CONFLICT,
                    "Log file is not ready yet. Status: " + status.getStatus());
        }

        Path filePath = null;
        try {
            filePath = Paths.get(status.getMessage());
            logger.debug("Attempting to download file from path: {}", filePath);
            Resource resource = new FileSystemResource(filePath);

            if (!resource.exists() || !resource.isReadable()) {
                logger.error("Filtered log file not found or cannot be read at path: {}", filePath);
                throw new ResponseStatusException(HttpStatus.NOT_FOUND,
                        "Filtered log file not found or cannot be read for task ID: " + taskId);
            }

            HttpHeaders headers = new HttpHeaders();
            headers.add(HttpHeaders.CONTENT_DISPOSITION,
                    "attachment; filename=\"" + resource.getFilename() + "\"");

            logger.info("Sending filtered log file {} for task ID {}",
                    resource.getFilename(), taskId);
            return ResponseEntity.ok()
                    .headers(headers)
                    .contentLength(resource.contentLength())
                    .body(resource);

        } catch (IOException e) {
            logger.error("IO Error accessing filtered log file for task ID {} at path {}: {}",
                    taskId, filePath, e.getMessage());
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
                    "Error accessing filtered log file.", e);
        } catch (Exception e) {
            logger.error("Unexpected error during download for task ID {}: {}",
                    taskId, e.getMessage());
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
                    "Unexpected error preparing download.", e);
        }
    }
}