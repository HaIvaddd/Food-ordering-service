package by.bsuir.foodordering.core.visitcounter;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;
import org.springframework.stereotype.Component;

@Component
public class VisitCounter {

    private final ConcurrentHashMap<String, AtomicLong> visitCounts = new ConcurrentHashMap<>();

    public void incrementVisit(String url) {
        AtomicLong counter = visitCounts.computeIfAbsent(url, k -> new AtomicLong(0));
        counter.incrementAndGet();
    }

    public Map<String, AtomicLong> getAllCounts() {
        return visitCounts;
    }
}
