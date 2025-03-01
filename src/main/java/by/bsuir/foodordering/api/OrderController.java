package by.bsuir.foodordering.api;

import by.bsuir.foodordering.api.dto.create.CreateOrderDto;
import by.bsuir.foodordering.api.dto.get.OrderDto;
import by.bsuir.foodordering.core.service.impl.OrderService;
import java.util.List;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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

    @PostMapping("/create")
    public OrderDto createOrder(@RequestBody CreateOrderDto createOrderDto) {
        return orderService.create(createOrderDto);
    }

    @DeleteMapping("/del/{id}")
    public void deleteOrder(@PathVariable("id") Long id) {
        orderService.deleteById(id);
    }

    @PutMapping("/update")
    public OrderDto updateOrder(@RequestBody OrderDto orderDto) {
        return orderService.update(orderDto);
    }
}
