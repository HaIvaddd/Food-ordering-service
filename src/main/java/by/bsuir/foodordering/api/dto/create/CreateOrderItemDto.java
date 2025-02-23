package by.bsuir.foodordering.api.dto.create;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateOrderItemDto {
    private Long foodId;
    private Integer count;
}
