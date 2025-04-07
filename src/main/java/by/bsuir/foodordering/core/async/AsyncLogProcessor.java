package by.bsuir.foodordering.core.async;

import by.bsuir.foodordering.core.service.impl.AsyncLogService;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Component
public class AsyncLogProcessor {

    private static final Logger log = LoggerFactory.getLogger(AsyncLogProcessor.class);

    @Async
    public void processLogCreation(String taskId,
                                   String logContent,
                                   Path logDirectory,
                                   Map<String, AsyncLogService.LogTaskStatus> taskStatuses) {
        taskStatuses.computeIfPresent(taskId, (id, status) -> {
            status.setStatus(AsyncLogService.Status.IN_PROGRESS);
            status.setMessage("Processing...");
            return status;
        });
        log.info("Task {} processing started.", taskId);

        try {
            TimeUnit.SECONDS.sleep(10);

            Path logFilePath = logDirectory.resolve("log_" + taskId + ".txt");
            Files.writeString(logFilePath, logContent);

            taskStatuses.computeIfPresent(taskId, (id, status) -> {
                status.setStatus(AsyncLogService.Status.COMPLETED);
                status.setMessage(logFilePath.toString());
                return status;
            });
            log.info("Task {} completed successfully. Log file: {}", taskId, logFilePath);

        } catch (IOException e) {
            log.error("Task {} failed during log file creation.", taskId, e);
            taskStatuses.computeIfPresent(taskId, (id, status) -> {
                status.setStatus(AsyncLogService.Status.FAILED);
                status.setMessage("Error creating file: " + e.getMessage());
                return status;
            });
        } catch (InterruptedException e) {
            log.error("Task {} was interrupted.", taskId, e);
            Thread.currentThread().interrupt();
            taskStatuses.computeIfPresent(taskId, (id, status) -> {
                status.setStatus(AsyncLogService.Status.FAILED);
                status.setMessage("Task interrupted.");
                return status;
            });
        }
    }
}