package by.bsuir.food_ordering_service.api.dto;

import java.time.LocalDateTime;
import java.util.List;

public class OrderDTO {
    private Long id;
    private Long userId;
    private LocalDateTime createdAt;
    private List<Long> orderItemIds;
}
