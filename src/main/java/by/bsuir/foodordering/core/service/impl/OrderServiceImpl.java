package by.bsuir.foodordering.core.service.impl;

import by.bsuir.foodordering.api.dto.create.CreateOrderDto;
import by.bsuir.foodordering.api.dto.get.OrderDto;
import by.bsuir.foodordering.api.dto.get.OrderInfoDto;
import by.bsuir.foodordering.core.exception.EntityNotFoundException;
import by.bsuir.foodordering.core.mapper.create.CreateOrderItemMapper;
import by.bsuir.foodordering.core.mapper.get.OrderInfoMapper;
import by.bsuir.foodordering.core.mapper.get.OrderItemMapper;
import by.bsuir.foodordering.core.mapper.get.OrderMapper;
import by.bsuir.foodordering.core.models.Order;
import by.bsuir.foodordering.core.models.OrderItem;
import by.bsuir.foodordering.core.repository.FoodRepository;
import by.bsuir.foodordering.core.repository.OrderItemRepository;
import by.bsuir.foodordering.core.repository.OrderRepository;
import by.bsuir.foodordering.core.repository.UserRepository;
import by.bsuir.foodordering.core.service.OrderService;
import jakarta.transaction.Transactional;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class OrderServiceImpl implements OrderService {

    private static final String ORDER_EX = "Order not found with id: ";

    private final OrderInfoMapper orderInfoMapper;
    private final OrderMapper orderMapper;
    private final FoodRepository foodRepository;
    private final OrderRepository orderRepository;
    private final OrderItemRepository orderItemRepository;
    private final OrderItemMapper orderItemMapper;
    private final CreateOrderItemMapper createOrderItemMapper;
    private final UserRepository userRepository;

    private List<OrderItem> toOrderItemMap(List<Long> orderItemIds) {
        return orderItemIds
                .stream()
                .map(
                        id -> orderItemRepository
                                .findOrderItemById(id)
                                .orElseThrow(
                                        () -> new EntityNotFoundException(
                                                "OrderItem not found with id: " + id
                                        )
                                )
                )
                .toList();
    }

    @Override
    @Transactional
    public OrderDto update(OrderDto orderDto) {
        if (orderDto == null) {
            throw new IllegalArgumentException();
        }
        Order order = orderRepository.findById(orderDto.getId())
                .orElseThrow(
                        () -> new EntityNotFoundException(
                                ORDER_EX + orderDto.getId()
                        )
                );
        if (!order.getUser().getId().equals(orderDto.getUserId())) {
            order.setUser(userRepository.findById(orderDto.getUserId())
                                    .orElseThrow(
                                            () -> new EntityNotFoundException(
                                                            "User not found with id: "
                                                                    + orderDto.getUserId()
                                            )
                                    )
            );
        }
        if (!orderDto.getOrderItemIds().isEmpty()) {
            order.setOrderItems(toOrderItemMap(orderDto.getOrderItemIds()));
        }
        return orderMapper.toDto(order);
    }

    @Override
    public OrderDto create(CreateOrderDto createOrderDto) {
        Order newOrder = new Order();
        newOrder.setUser(userRepository.findById(createOrderDto.getUserId())
                .orElseThrow(
                        () -> new EntityNotFoundException(
                                "User not found with id: " + createOrderDto.getUserId())
                )
        );
        newOrder.setCreatedAt(LocalDateTime.now());
        newOrder.setOrderItems(createOrderDto.getCreateOrderItems()
                .stream().map(dto -> createOrderItemMapper.toEntity(dto, foodRepository))
                        .toList());
        newOrder.getOrderItems().forEach(orderItem -> orderItem.setOrder(newOrder));
        newOrder.getOrderItems().forEach(orderItem -> orderItem
                .setTotalPrice(BigDecimal
                        .valueOf(orderItem.getCount())
                        .multiply(orderItem.getFood().getPrice())));
        return orderMapper.toDto(orderRepository.save(newOrder));
    }

    @Override
    public List<OrderDto> findByUserId(Long userId) {
        return orderMapper.toDtos(orderRepository.findByUserId(userId));
    }

    @Override
    public void deleteById(Long id) {
        if (orderRepository.existsById(id)) {
            orderRepository.deleteById(id);
        } else {
            throw new EntityNotFoundException(ORDER_EX + id);
        }
    }

    @Override
    public List<OrderInfoDto> findAll() {
        return orderRepository
                .findAll()
                .stream()
                .map(order -> orderInfoMapper.toDto(order, orderItemMapper)).toList();
    }

    @Override
    public OrderDto findById(Long id) {
        return orderMapper.toDto(orderRepository
                .findById(id)
                .orElseThrow(() -> new EntityNotFoundException(ORDER_EX + id)));
    }
}
