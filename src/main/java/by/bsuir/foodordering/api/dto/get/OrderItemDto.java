package by.bsuir.foodordering.api.dto.get;

import java.math.BigDecimal;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OrderItemDto {
    private FoodDto foodDto;
    private Integer count;
    private BigDecimal totalPrice;
}
