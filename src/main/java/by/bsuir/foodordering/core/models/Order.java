package by.bsuir.foodordering.core.models;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class Order {
    private Long id;

    private User user;
    private LocalDateTime createdAt;
    private final List<OrderItem> orderItems = new ArrayList<>();

    public Order(Long id, User user,  LocalDateTime createdAt, List<OrderItem> orderItems) {
        this.id = id;
        this.user = user;
        this.createdAt = createdAt;
        this.orderItems.addAll(orderItems);
    }

    public void setOrderItems(List<OrderItem> orderItems) {
        this.orderItems.clear();
        this.orderItems.addAll(orderItems);
    }

    public void addOrderItems(List<OrderItem> orderItems) {
        this.orderItems.addAll(orderItems);
    }
}
