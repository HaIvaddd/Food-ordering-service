package by.bsuir.foodordering.api.dto.create;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import java.util.List;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateOrderDto {
    @NotNull(message = "User Id cannot be null")
    @Positive(message = "User Id must be positive")
    private Long userId;
    @NotNull(message = "Order Items cannot be null")
    private List<CreateOrderItemDto> createOrderItems;
}
