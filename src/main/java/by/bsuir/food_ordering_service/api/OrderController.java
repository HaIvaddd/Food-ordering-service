package by.bsuir.food_ordering_service.api;

import by.bsuir.food_ordering_service.api.dto.OrderDTO;
import by.bsuir.food_ordering_service.core.service.OrderService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("api/orders")
@AllArgsConstructor
public class OrderController {

    private final OrderService orderService;

    @GetMapping
    public List<OrderDTO> findAll() {
        return orderService.findAll();
    }
}
