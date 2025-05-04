package by.bsuir.foodordering.api.dto.create;

import by.bsuir.foodordering.api.dto.create.CreateOrderItemDto;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class UpdateOrderDto {

    @NotNull(message = "Order ID cannot be null for update")
    private Long orderId; // ID редактируемого заказа

    @NotNull(message = "User ID cannot be null")
    @Positive(message = "User ID must be positive")
    private Long userId;

    @NotNull(message = "Order Items cannot be null")
    @Valid // Валидируем и элементы списка
    private List<CreateOrderItemDto> createOrderItems; // Используем тот же DTO для позиций
}