package by.bsuir.foodordering.core.service.impl;

import by.bsuir.foodordering.core.async.AsyncLogProcessor;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
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
    // Директория для РЕЗУЛЬТИРУЮЩИХ отфильтрованных логов
    private static final Path TARGET_LOG_DIRECTORY = Paths.get("filtered-app-logs"); // Немного другое имя
    private final Map<String, LogTaskStatus> taskStatuses = new ConcurrentHashMap<>();

    private final AsyncLogProcessor asyncLogProcessor;

    public enum Status { PENDING, IN_PROGRESS, COMPLETED, FAILED }

    @Setter
    @Getter
    @AllArgsConstructor
    public static class LogTaskStatus {
        private Status status;
        private String message; // Путь к файлу или сообщение об ошибке/статусе
    }

    public AsyncLogService(final AsyncLogProcessor asyncLogProcessor) {
        this.asyncLogProcessor = asyncLogProcessor;
        try {
            if (!Files.exists(TARGET_LOG_DIRECTORY)) {
                Files.createDirectories(TARGET_LOG_DIRECTORY);
                log.info("Created directory for filtered application logs: {}", TARGET_LOG_DIRECTORY.toAbsolutePath());
            }
        } catch (IOException e) {
            log.error("Could not create filtered application log directory: {}", TARGET_LOG_DIRECTORY, e);
        }
    }

    /**
     * Инициирует асинхронную фильтрацию логов приложения по дате.
     * @param dateString Дата в формате YYYY-MM-DD
     * @return Уникальный ID задачи
     * @throws DateTimeParseException Если формат даты некорректный
     */
    public String filterAppLogByDateAsync(String dateString) throws DateTimeParseException {
        // Предварительная валидация формата даты
        LocalDate.parse(dateString, DateTimeFormatter.ISO_LOCAL_DATE);

        String taskId = UUID.randomUUID().toString();
        taskStatuses.put(taskId, new LogTaskStatus(Status.PENDING, "Task queued for filtering application logs by date: " + dateString));
        log.info("Task {} queued for filtering application logs by date {}.", taskId, dateString);

        // Вызываем соответствующий метод асинхронного процессора
        asyncLogProcessor.processAppLogFiltering(taskId, dateString, TARGET_LOG_DIRECTORY, taskStatuses);

        return taskId;
    }

    // Метод createLogFileAsync из предыдущей задачи можно оставить, если он нужен,
    // или удалить, если теперь только фильтрация по дате.

    public LogTaskStatus getTaskStatus(String taskId) {
        return taskStatuses.get(taskId);
    }
}