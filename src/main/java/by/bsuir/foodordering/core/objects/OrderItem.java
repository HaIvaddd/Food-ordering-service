package by.bsuir.foodordering.core.objects;

import java.math.BigDecimal;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

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
