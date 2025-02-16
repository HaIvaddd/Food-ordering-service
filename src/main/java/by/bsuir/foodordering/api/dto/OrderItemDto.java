package by.bsuir.foodordering.api.dto;

import java.math.BigDecimal;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OrderItemDto {
    private FoodDto food;
    private Integer count;
    private BigDecimal totalPrice;
}
