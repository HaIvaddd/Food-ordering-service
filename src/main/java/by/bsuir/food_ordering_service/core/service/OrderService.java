package by.bsuir.food_ordering_service.core.service;

import by.bsuir.food_ordering_service.api.dto.OrderDTO;
import by.bsuir.food_ordering_service.core.mapper.OrderMapper;
import by.bsuir.food_ordering_service.core.repository.OrderRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final OrderMapper orderMapper;

    public List<OrderDTO> findAll() {
        return orderMapper.toDTOs(orderRepository.findAll());
    }
}
