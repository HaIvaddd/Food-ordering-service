package by.bsuir.foodordering.core.service;

import by.bsuir.foodordering.api.dto.create.CreateOrderDto;
import by.bsuir.foodordering.api.dto.get.OrderDto;
import java.util.List;

public interface BaseOrderService {

    List<OrderDto> findAll();

    List<OrderDto> findByUserId(Long userId);

    OrderDto update(OrderDto orderDto);

    OrderDto findById(Long id);

    OrderDto create(CreateOrderDto createOrderDto);

    void deleteById(Long id);
}
