package by.bsuir.foodordering.core.objects;

import java.math.BigDecimal;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
public class Food {

    private Long id;

    private String name;
    private BigDecimal price;

    private FoodType foodType;

    public boolean hasType(String type) {
        return foodType.equals(FoodType.valueOf(type));
    }

}