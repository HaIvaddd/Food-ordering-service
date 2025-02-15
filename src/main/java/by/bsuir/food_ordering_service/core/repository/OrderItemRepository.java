package by.bsuir.food_ordering_service.core.repository;

import by.bsuir.food_ordering_service.core.objects.OrderItem;
import lombok.Getter;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.Map;

@Repository
@Getter
public class OrderItemRepository {

    private final Map<Long, OrderItem> orderItems = new HashMap<>();

    public OrderItem getOrderItemById(Long orderItemId) {
        return orderItems.get(orderItemId);
    }

    public OrderItem saveOrderItem(OrderItem orderItem) {
        return orderItems.put(orderItem.getId(), orderItem);
    }

}
