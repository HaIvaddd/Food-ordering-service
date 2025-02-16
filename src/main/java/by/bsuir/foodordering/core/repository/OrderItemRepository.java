package by.bsuir.foodordering.core.repository;

import by.bsuir.foodordering.core.objects.OrderItem;
import java.util.HashMap;
import java.util.Map;
import lombok.Getter;
import org.springframework.stereotype.Repository;



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
