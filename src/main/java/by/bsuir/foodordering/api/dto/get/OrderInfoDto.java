package by.bsuir.foodordering.api.dto.get;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class OrderInfoDto {
    private Long id;
    private UserDto userDto;
    private LocalDateTime createdAt;
    private List<OrderItemDto> orderItemDtos;
    private BigDecimal totalPrice;
    private boolean isOrdered;
}
