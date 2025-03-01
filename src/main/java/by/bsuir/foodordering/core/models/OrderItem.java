package by.bsuir.foodordering.core.models;

import java.math.BigDecimal;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class OrderItem {

    private Long id;

    private Order order;
    private Food food;
    private Integer count;

    private BigDecimal totalPrice;
}
