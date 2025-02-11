package by.bsuir.food_ordering_service.core.objects.food;

import by.bsuir.food_ordering_service.core.objects.OrderItem;
import lombok.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;


@Getter
@RequiredArgsConstructor
@NoArgsConstructor
public abstract class AFood {

    protected Long id;

    @NonNull @Setter
    protected String name;
    @NonNull @Setter
    protected BigDecimal price;

    List<OrderItem> orderItems = new ArrayList<>();
}
