package by.bsuir.foodordering.api.dto.create;

import java.math.BigDecimal;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateFoodDto {

    private String name;
    private BigDecimal price;

    private String foodTypeStr;
}
