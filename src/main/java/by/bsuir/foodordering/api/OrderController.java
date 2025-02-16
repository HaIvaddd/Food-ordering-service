package by.bsuir.foodordering.api;

import by.bsuir.foodordering.api.dto.OrderDto;
import by.bsuir.foodordering.core.service.OrderService;
import java.util.List;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/order")
@AllArgsConstructor
public class OrderController {

    private final OrderService orderService;

    @GetMapping
    public List<OrderDto> findAll() {
        return orderService.findAll();
    }

    @GetMapping({"/{id}"})
    public OrderDto findById(@PathVariable Long id) {
        return orderService.findById(id);
    }
}
