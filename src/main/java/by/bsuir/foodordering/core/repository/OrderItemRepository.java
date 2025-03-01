package by.bsuir.foodordering.core.repository;

import by.bsuir.foodordering.core.models.OrderItem;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import lombok.Getter;
import org.springframework.stereotype.Repository;

@Repository
@Getter
public class OrderItemRepository {

    private final Map<Long, OrderItem> orderItems = new HashMap<>();

    public Optional<OrderItem> getOrderItemById(Long orderItemId) {
        return Optional.of(orderItems.get(orderItemId));
    }

    public OrderItem saveOrderItem(OrderItem orderItem) {
        return orderItems.put(orderItem.getId(), orderItem);
    }

}
