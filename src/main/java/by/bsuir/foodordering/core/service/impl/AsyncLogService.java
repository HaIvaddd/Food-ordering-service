package by.bsuir.foodordering.core.service.impl;

import by.bsuir.foodordering.core.async.AsyncLogProcessor;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class AsyncLogService {

    private static final Logger log = LoggerFactory.getLogger(AsyncLogService.class);
    private static final Path LOG_DIRECTORY = Paths.get("logs");
    private final Map<String, LogTaskStatus> taskStatuses = new ConcurrentHashMap<>();

    private final AsyncLogProcessor asyncLogProcessor;

    public enum Status { PENDING, IN_PROGRESS, COMPLETED, FAILED }

    @Setter
    @Getter
    @AllArgsConstructor
    public static class LogTaskStatus {
        private Status status;
        private String message;
    }


    public AsyncLogService(final AsyncLogProcessor asyncLogProcessor) {
        this.asyncLogProcessor = asyncLogProcessor;
        try {
            if (!Files.exists(LOG_DIRECTORY)) {
                Files.createDirectories(LOG_DIRECTORY);
            }
        } catch (IOException e) {
            log.error("Could not create log directory: {}", LOG_DIRECTORY, e);
        }
    }

    public String createLogFileAsync(String logContent) {
        String taskId = UUID.randomUUID().toString();
        taskStatuses.put(taskId, new LogTaskStatus(Status.PENDING, "Task queued"));
        log.info("Task {} queued for log creation.", taskId);

        asyncLogProcessor.processLogCreation(taskId, logContent, LOG_DIRECTORY, taskStatuses);

        return taskId;
    }

    public LogTaskStatus getTaskStatus(String taskId) {
        return taskStatuses.get(taskId);
    }
}
