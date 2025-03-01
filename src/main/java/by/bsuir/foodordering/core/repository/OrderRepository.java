package by.bsuir.foodordering.core.repository;

import by.bsuir.foodordering.core.models.Order;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import lombok.Getter;
import org.springframework.stereotype.Repository;

@Repository
@Getter
public class OrderRepository {

    private final Map<Long, Order> orders = new HashMap<>();

    public Optional<Order> findByOrderId(Long id) {
        return Optional.of(orders.get(id));
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

    public void deleteById(Long id) {
        orders.remove(id);
    }

    public Order save(Order order) {
        return orders.put(order.getId(), order);
    }

    public boolean existsById(Long id) {
        return orders.containsKey(id);
    }
}
