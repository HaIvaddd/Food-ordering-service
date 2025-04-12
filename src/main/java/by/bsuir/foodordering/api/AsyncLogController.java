
package by.bsuir.foodordering.api;

import by.bsuir.foodordering.core.service.impl.AsyncLogService;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import lombok.AllArgsConstructor;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

@RestController
@AllArgsConstructor
@RequestMapping("/api/logs")
public class AsyncLogController {

    private final AsyncLogService asyncLogService;

    @PostMapping("/create")
    public ResponseEntity<String> createLog(@RequestBody String logContent) {
        if (logContent == null || logContent.isEmpty()) {
            return ResponseEntity.badRequest().body("Log content cannot be empty");
        }
        String taskId = asyncLogService.createLogFileAsync(logContent);
        return ResponseEntity.accepted().body(taskId);
    }

    @GetMapping("/{taskId}/status")
    public ResponseEntity<AsyncLogService.LogTaskStatus> getLogStatus(@PathVariable String taskId) {
        AsyncLogService.LogTaskStatus status = asyncLogService.getTaskStatus(taskId);
        if (status == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,
                    "Task not found with ID: " + taskId);
        }
        return ResponseEntity.ok(status);
    }

    @GetMapping("/{taskId}/download")
    public ResponseEntity<Resource> downloadLogFile(@PathVariable String taskId) {
        AsyncLogService.LogTaskStatus status = asyncLogService.getTaskStatus(taskId);
        if (status == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,
                    "Task not found with ID: " + taskId);
        }

        if (status.getStatus() != AsyncLogService.Status.COMPLETED) {
            throw new ResponseStatusException(HttpStatus.CONFLICT,
                    "Log file is not ready yet. Status: " + status.getStatus());
        }

        try {
            Path filePath = Paths.get(status.getMessage());
            Resource resource = new FileSystemResource(filePath);

            if (!resource.exists() || !resource.isReadable()) {
                throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
                        "Log file not found or cannot be read.");
            }

            HttpHeaders headers = new HttpHeaders();
            headers.add(HttpHeaders.CONTENT_DISPOSITION,
                    "attachment; filename=\"" + resource.getFilename() + "\"");
            headers.add(HttpHeaders.CONTENT_TYPE, MediaType.TEXT_PLAIN_VALUE);

            return ResponseEntity.ok()
                    .headers(headers)
                    .contentLength(resource.contentLength())
                    .body(resource);

        } catch (IOException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
                    "Error accessing log file.", e);
        }
    }
}
