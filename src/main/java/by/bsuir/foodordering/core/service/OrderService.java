package by.bsuir.foodordering.core.service;

import by.bsuir.foodordering.api.dto.OrderDto;
import by.bsuir.foodordering.core.mapper.OrderMapper;
import by.bsuir.foodordering.core.repository.OrderRepository;
import java.util.List;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;



@Service
@AllArgsConstructor
public class OrderService implements BaseOrderService {

    private final OrderRepository orderRepository;
    private final OrderMapper orderMapper;

    @Override
    public List<OrderDto> findAll() {
        return orderMapper.toDtos(orderRepository.findAll());
    }

    @Override
    public OrderDto findById(Long id) {
        return orderMapper.toDto(orderRepository.findByOrderId(id));
    }
}
