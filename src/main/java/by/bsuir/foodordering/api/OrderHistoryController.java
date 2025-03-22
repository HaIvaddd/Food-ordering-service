package by.bsuir.foodordering.api;

import by.bsuir.foodordering.core.models.OrderHistoryItem;
import by.bsuir.foodordering.core.service.OrderHistoryService;
import java.util.List;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/history")
@AllArgsConstructor
public class OrderHistoryController {
    private final OrderHistoryService orderHistoryService;

    @GetMapping
    public List<OrderHistoryItem> findAll() {
        return orderHistoryService.findAll();
    }
}
