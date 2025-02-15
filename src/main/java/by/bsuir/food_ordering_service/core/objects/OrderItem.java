package by.bsuir.food_ordering_service.core.objects;

import lombok.*;

import java.math.BigDecimal;

@Setter
@Getter
@AllArgsConstructor
public class OrderItem {

    private Long id;

    private Order order;
    private Food food;
    private Integer count;

    private BigDecimal totalPrice;
}
