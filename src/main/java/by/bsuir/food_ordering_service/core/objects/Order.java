package by.bsuir.food_ordering_service.core.objects;

import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;


@Getter @RequiredArgsConstructor
public class Order {

    private Long id;

    @NonNull @Setter
    private User user;
    @NonNull @Setter
    private LocalDateTime createdAt;
    //Возможно добавить цену всего заказа
    private List<OrderItem> orderItemList = new ArrayList<>();
}
