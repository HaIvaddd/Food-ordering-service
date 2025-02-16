package by.bsuir.foodordering.api.dto;

import java.math.BigDecimal;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class FoodDto {

    private Long id;

    private String name;
    private BigDecimal price;

    private String foodTypeStr;
}
