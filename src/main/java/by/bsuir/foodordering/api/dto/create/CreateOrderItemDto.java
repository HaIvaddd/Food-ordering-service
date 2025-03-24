package by.bsuir.foodordering.api.dto.create;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateOrderItemDto {
    @NotNull(message = "Food Id cannot be null")
    @Positive(message = "Food Id must be positive")
    private Long foodId;
    @NotNull(message = "Count cannot be null")
    @Positive(message = "Count must be positive")
    private Integer count;
}
