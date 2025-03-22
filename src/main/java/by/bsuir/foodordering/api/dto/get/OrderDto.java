package by.bsuir.foodordering.api.dto.get;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class OrderDto {
    private Long id;
    private Long userId;
    private LocalDateTime createdAt;
    private List<Long> orderItemIds;
    private BigDecimal totalPrice;
    private boolean isOrdered;
}