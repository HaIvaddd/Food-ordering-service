package by.bsuir.foodordering.api.dto.create;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import java.math.BigDecimal;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateFoodDto {
    @NotBlank(message = "Food name cannot be blank")
    private String name;
    @NotNull(message = "Price cannot be null")
    @Positive(message = "Price must be positive")
    private BigDecimal price;
    @NotBlank(message = "Food type cannot be blank")
    private String foodTypeStr;
}
