package by.bsuir.food_ordering_service.core.objects;

import by.bsuir.food_ordering_service.core.objects.food.AFood;
import lombok.*;

import java.math.BigDecimal;

@RequiredArgsConstructor @Getter
public class OrderItem {

    private Long id;

    @Setter @NonNull
    private Order order;
    @Setter @NonNull
    private AFood food;
    @Setter @NonNull
    private Integer count;

    @Setter
    private BigDecimal totalPrice;
}
