package by.bsuir.foodordering.visitcounter;

import by.bsuir.foodordering.core.visitcounter.VisitCounter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Timeout;

import java.util.Map;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

class VisitCounterTest {

    private VisitCounter visitCounter;

    @BeforeEach
    void setUp() {
        // Создаем новый экземпляр перед каждым тестом
        visitCounter = new VisitCounter();
    }

    @Test
    void incrementVisit_singleThread_shouldIncrementCorrectly() {
        // Простой базовый тест для одного потока
        String url1 = "/api/foods/1";
        String url2 = "/api/foods/2";

        visitCounter.incrementVisit(url1);
        visitCounter.incrementVisit(url1);
        visitCounter.incrementVisit(url2);

        Map<String, AtomicLong> counts = visitCounter.getAllCounts();
        assertNotNull(counts.get(url1));
        assertEquals(2L, counts.get(url1).get());
        assertNotNull(counts.get(url2));
        assertEquals(1L, counts.get(url2).get());
    }

    @Test
    @Timeout(value = 5, unit = TimeUnit.SECONDS)
    void incrementVisit_multiThread_shouldIncrementCorrectly() throws InterruptedException {
        final int numThreads = 10;
        final int incrementsPerThread = 1000;
        final String url1 = "/api/foods/bulk";
        final String url2 = "/api/foods/by-name?name=Pizza";
        final int totalIncrementsUrl1 = numThreads * incrementsPerThread;
        final int totalIncrementsUrl2 = numThreads * incrementsPerThread;

        ExecutorService executorService = Executors.newFixedThreadPool(numThreads);
        CountDownLatch startLatch = new CountDownLatch(1);
        CountDownLatch endLatch = new CountDownLatch(numThreads);

        for (int i = 0; i < numThreads; i++) {
            executorService.submit(() -> {
                try {
                    startLatch.await();

                    for (int j = 0; j < incrementsPerThread; j++) {
                        visitCounter.incrementVisit(url1);
                        visitCounter.incrementVisit(url2);
                    }
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                } finally {
                    endLatch.countDown();
                }
            });
        }

        startLatch.countDown();

        assertTrue(endLatch.await(4, TimeUnit.SECONDS), "Test timed out waiting for threads to finish");

        executorService.shutdown();
        assertTrue(executorService.awaitTermination(1, TimeUnit.SECONDS), "Executor service did not terminate");

        Map<String, AtomicLong> counts = visitCounter.getAllCounts();
        assertNotNull(counts.get(url1), "Count for URL1 should exist");
        assertEquals(totalIncrementsUrl1, counts.get(url1).get(), "Incorrect total count for URL1");

        assertNotNull(counts.get(url2), "Count for URL2 should exist");
        assertEquals(totalIncrementsUrl2, counts.get(url2).get(), "Incorrect total count for URL2");
    }
}