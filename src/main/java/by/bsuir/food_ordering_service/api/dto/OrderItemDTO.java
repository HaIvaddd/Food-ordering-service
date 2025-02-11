package by.bsuir.food_ordering_service.api.dto;

import by.bsuir.food_ordering_service.api.dto.food.AFoodDTO;

import java.math.BigDecimal;

public class OrderItemDTO {
    private AFoodDTO food;
    private Integer count;
    private BigDecimal totalPrice;
}
