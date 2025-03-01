package by.bsuir.foodordering.core.models;

import by.bsuir.foodordering.core.exception.FoodTypeException;
import java.math.BigDecimal;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "Foods")
public class Food {

    private Long id;

    private String name;
    private BigDecimal price;

    private FoodType foodType;

    public boolean hasType(String type) {
        try {
            return foodType.equals(FoodType.valueOf(type));
        } catch (IllegalArgumentException e) {
            throw new FoodTypeException("Unknown FoodType: " + type);
        }
    }
}