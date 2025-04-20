package by.bsuir.foodordering.core.async;

import by.bsuir.foodordering.core.service.impl.AsyncLogService;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Component
public class AsyncLogProcessor {

    private static final Logger log = LoggerFactory.getLogger(AsyncLogProcessor.class);

    @Value("${log.file.retrieve.path}")
    private String sourceLogFilePath;

    @Async
    public void processAppLogFiltering(String taskId,
                                       String dateString,
                                       Path targetLogDirectory,
                                       Map<String, AsyncLogService.LogTaskStatus> taskStatuses) {
        taskStatuses.computeIfPresent(taskId, (id, status) -> {
            status.setStatus(AsyncLogService.Status.IN_PROGRESS);
            status.setMessage("Filtering application logs for date: " + dateString);
            return status;
        });
        log.info("Task {} started: Filtering application logs for date {}.", taskId, dateString);

        LocalDate targetDate;

        try {
            log.info("Task {} is now IN_PROGRESS, pausing for testing...", taskId);
            TimeUnit.SECONDS.sleep(15);
            targetDate = LocalDate.parse(dateString, DateTimeFormatter.ISO_LOCAL_DATE);
            log.info("Task {} resuming processing after pause.", taskId);
        } catch (InterruptedException e) {
            log.error("Task {} sleep interrupted.", taskId, e);
            Thread.currentThread().interrupt();
            taskStatuses.computeIfPresent(taskId, (id, status) -> {
                status.setStatus(AsyncLogService.Status.FAILED);
                status.setMessage("Task interrupted during initial pause.");
                return status;
            });
            return;
        }

        Path sourcePath = Paths.get(sourceLogFilePath);
        String targetFilename = "app-logs-" + dateString + "-" + taskId + ".log";
        Path destinationPath = targetLogDirectory.resolve(targetFilename);

        long linesProcessed = 0;
        long linesMatched = 0;

        if (!Files.exists(sourcePath)) {
            log.error("Task {} failed: Source application log file not found at '{}'.",
                    taskId, sourcePath.toAbsolutePath());
            taskStatuses.computeIfPresent(taskId, (id, status) -> {
                status.setStatus(AsyncLogService.Status.FAILED);
                status.setMessage("Source application log file not found: " + sourceLogFilePath);
                return status;
            });
            return;
        }
        if (!Files.isReadable(sourcePath)) {
            log.error("Task {} failed: Source application log file not readable at '{}'.",
                    taskId, sourcePath.toAbsolutePath());
            taskStatuses.computeIfPresent(taskId, (id, status) -> {
                status.setStatus(AsyncLogService.Status.FAILED);
                status.setMessage("Source application log file not readable: " + sourceLogFilePath);
                return status;
            });
            return;
        }

        try (BufferedReader reader = Files.newBufferedReader(sourcePath, StandardCharsets.UTF_8);
             BufferedWriter writer =
                     Files.newBufferedWriter(destinationPath, StandardCharsets.UTF_8)) {

            String line;
            String datePattern = targetDate.format(DateTimeFormatter.ISO_LOCAL_DATE);

            while ((line = reader.readLine()) != null) {
                linesProcessed++;
                if (line.contains(datePattern)) {
                    writer.write(line);
                    writer.newLine();
                    linesMatched++;
                }
            }

            if (linesMatched == 0) {
                log.warn("Task {} completed, but no logs found for date {}.", taskId, dateString);
                taskStatuses.computeIfPresent(taskId, (id, status) -> {
                    status.setStatus(AsyncLogService.Status.COMPLETED);
                    status.setMessage(destinationPath.toString());
                    return status;
                });
            } else {
                taskStatuses.computeIfPresent(taskId, (id, status) -> {
                    status.setStatus(AsyncLogService.Status.COMPLETED);
                    status.setMessage(destinationPath.toString());
                    return status;
                });
                log.info(
                    "Task {} completed successfully. Filtered {} lines out of {}. Output file: {}",
                    taskId, linesMatched, linesProcessed, destinationPath);
            }


        } catch (NoSuchFileException e) {
            log.error("Task {} failed: Source log file disappeared during processing: {}",
                    taskId, sourcePath.toAbsolutePath(), e);
            taskStatuses.computeIfPresent(taskId, (id, status) -> {
                status.setStatus(AsyncLogService.Status.FAILED);
                status.setMessage("Source log file disappeared during processing.");
                return status;
            });
        } catch (IOException e) {
            log.error("Task {} failed during log file processing.", taskId, e);
            taskStatuses.computeIfPresent(taskId, (id, status) -> {
                status.setStatus(AsyncLogService.Status.FAILED);
                status.setMessage("Error processing log file: " + e.getMessage());
                return status;
            });
        }
    }
}