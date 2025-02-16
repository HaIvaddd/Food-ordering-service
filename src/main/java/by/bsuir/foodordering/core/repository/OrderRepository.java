package by.bsuir.foodordering.core.repository;

import by.bsuir.foodordering.core.objects.Order;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.Getter;
import org.springframework.stereotype.Repository;

@Repository
@Getter
public class OrderRepository {

    private final Map<Long, Order> orders = new HashMap<>();

    public Order findByOrderId(Long id) {
        return orders.get(id);
    }

    public List<Order> findByUserId(Long userId) {
        return orders.values()
                .stream()
                .filter(order -> order.getUser().getId().equals(userId))
                .toList();
    }

    public List<Order> findAll() {
        return new ArrayList<>(orders.values());
    }

    public Order save(Order order) {
        return orders.put(order.getId(), order);
    }
}
