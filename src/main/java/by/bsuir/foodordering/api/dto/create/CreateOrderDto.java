package by.bsuir.foodordering.api.dto.create;

import java.util.List;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateOrderDto {
    private Long userId;
    private List<CreateOrderItemDto> createOrderItems;
}
