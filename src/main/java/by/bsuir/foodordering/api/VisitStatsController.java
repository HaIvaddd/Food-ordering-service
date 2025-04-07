package by.bsuir.foodordering.api;

import by.bsuir.foodordering.core.visitcounter.VisitCounter;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
@RequestMapping("/api/visits")
public class VisitStatsController {

    private final VisitCounter visitCounter;

    @GetMapping("/stats")
    public ResponseEntity<Map<String, Long>> getVisitStats() {
        Map<String, AtomicLong> atomicStats = visitCounter.getAllCounts();

        Map<String, Long> stats = atomicStats.entrySet().stream()
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        entry -> entry.getValue().get()
                ));

        return ResponseEntity.ok(stats);
    }
}
