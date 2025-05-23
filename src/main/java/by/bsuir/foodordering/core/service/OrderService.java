package by.bsuir.foodordering.core.service;

import by.bsuir.foodordering.api.dto.create.CreateOrderDto;
import by.bsuir.foodordering.api.dto.create.UpdateOrderDto;
import by.bsuir.foodordering.api.dto.get.OrderDto;
import by.bsuir.foodordering.api.dto.get.OrderInfoDto;
import java.util.List;

public interface OrderService {

    List<OrderInfoDto> findAll();

    List<OrderInfoDto> findByCountFood(int count);

    List<OrderInfoDto> findByFoodName(String foodName);

    List<OrderDto> findByUserId(Long userId);

    void updateOrder(UpdateOrderDto dto);

    OrderInfoDto findById(Long id);

    OrderDto create(CreateOrderDto createOrderDto);

    OrderDto makeOrderById(Long id);

    void deleteById(Long id);
}
