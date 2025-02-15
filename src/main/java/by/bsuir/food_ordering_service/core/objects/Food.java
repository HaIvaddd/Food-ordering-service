package by.bsuir.food_ordering_service.core.objects.food;

import by.bsuir.food_ordering_service.core.objects.OrderItem;
import lombok.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Setter
@Getter
@RequiredArgsConstructor
@NoArgsConstructor
public abstract class AFood {

    protected Long id;

    protected String name;
    protected BigDecimal price;

    //List<OrderItem> orderItems = new ArrayList<>();
}
