package by.bsuir.food_ordering_service.core.objects;

import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@Getter
@Setter
public class Order {
    private Long id;

    private User user;
    private LocalDateTime createdAt;
    //Возможно добавить цену всего заказа
    private List<OrderItem> orderItems = new ArrayList<>();
}
