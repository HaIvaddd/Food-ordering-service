package by.bsuir.foodordering.core.objects;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Order {
    private Long id;

    private User user;
    private LocalDateTime createdAt;
    //Возможно добавить цену всего заказа
    private final List<OrderItem> orderItems = new ArrayList<>();

    public Order(Long id, User user,  LocalDateTime createdAt, List<OrderItem> orderItems) {
        this.id = id;
        this.user = user;
        this.createdAt = createdAt;
        this.orderItems.addAll(orderItems);
    }
}
