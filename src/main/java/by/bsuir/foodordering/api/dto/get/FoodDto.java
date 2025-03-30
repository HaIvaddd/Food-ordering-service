package by.bsuir.foodordering.api.dto.get;

import java.math.BigDecimal;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
public class FoodDto {

    private Long id;

    private String name;
    private BigDecimal price;

    private String foodTypeStr;
}
