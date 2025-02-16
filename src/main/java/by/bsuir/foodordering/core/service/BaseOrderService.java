package by.bsuir.foodordering.core.service;

import by.bsuir.foodordering.api.dto.OrderDto;
import java.util.List;

public interface BaseOrderService {

    OrderDto findById(Long id);

    List<OrderDto> findAll();
}
