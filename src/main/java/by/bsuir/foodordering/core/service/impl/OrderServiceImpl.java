package by.bsuir.foodordering.core.service.impl;

import by.bsuir.foodordering.api.dto.create.CreateOrderDto;
import by.bsuir.foodordering.api.dto.create.CreateOrderItemDto;
import by.bsuir.foodordering.api.dto.get.OrderDto;
import by.bsuir.foodordering.api.dto.get.OrderInfoDto;
import by.bsuir.foodordering.core.annotation.Timed;
import by.bsuir.foodordering.core.cache.Cache;
import by.bsuir.foodordering.core.exception.CreatedEntityException;
import by.bsuir.foodordering.core.exception.EntityNotFoundException;
import by.bsuir.foodordering.core.exception.MakeOrderException;
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
import java.util.ArrayList;
import java.util.Iterator;
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
    private final OrderHistoryServiceImpl orderHistoryService;
    private final Cache foodCache;

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

    private void updateOrdersList(List<Order> orders) {
        BigDecimal totalPrice;
        Iterator<Order> iterator = orders.iterator();

        while (iterator.hasNext()) {
            Order order = iterator.next();

            totalPrice = BigDecimal.ZERO;

            if (order.getOrderItems().isEmpty()) {
                iterator.remove();
                orderRepository.delete(order);
            } else {
                for (OrderItem orderItem : order.getOrderItems()) {
                    totalPrice = totalPrice.add(orderItem.getTotalPrice());
                }
                order.setTotalPrice(totalPrice);
            }
        }
    }

    @Override
    @Transactional
    public OrderDto update(OrderDto orderDto) {
        if (orderDto == null) {
            throw new IllegalArgumentException();
        } else if (orderDto.isOrdered()) {
            throw new MakeOrderException("The order has already been placed");
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
        BigDecimal totalPrice = BigDecimal.ZERO;

        for (OrderItem orderItem : order.getOrderItems()) {
            totalPrice = totalPrice.add(orderItem.getTotalPrice());
        }
        order.setTotalPrice(totalPrice);

        return orderMapper.toDto(order);
    }

    @Transactional
    @Override
    public OrderDto makeOrderById(Long id) {
        Order order = orderRepository
                .findById(id)
                .orElseThrow(() -> new EntityNotFoundException(ORDER_EX + id));
        order.setOrdered(true);
        orderHistoryService.addOrderToHistory(order);
        return orderMapper.toDto(order);
    }

    @Timed
    @Override
    public OrderDto create(CreateOrderDto createOrderDto) {
        if (createOrderDto == null || createOrderDto.getCreateOrderItems().isEmpty()) {
            throw new CreatedEntityException("Create order items cannot be empty");
        }

        Order newOrder = new Order();

        newOrder.setUser(userRepository.findById(createOrderDto.getUserId())
                .orElseThrow(
                        () -> new EntityNotFoundException(
                                "User not found with id: " + createOrderDto.getUserId())
                )
        );

        newOrder.setCreatedAt(LocalDateTime.now());

        List<OrderItem> orderItems = new ArrayList<>();
        OrderItem orderItem;
        Long foodId;
        BigDecimal totalPrice = BigDecimal.ZERO;

        for (CreateOrderItemDto orderItemDto : createOrderDto.getCreateOrderItems()) {

            foodId = orderItemDto.getFoodId();
            orderItem = createOrderItemMapper.toEntity(orderItemDto);

            if (foodCache.get(foodId)  != null) {
                orderItem.setFood(foodCache.get(foodId));
            } else {
                orderItem.setFood(foodCache.put(foodRepository
                        .findById(foodId)
                        .orElseThrow(
                                () -> new EntityNotFoundException(
                                        "Food not found with id: " + orderItemDto.getFoodId()
                                )
                        ))
                );
            }
            orderItem.setTotalPrice(
                    BigDecimal.valueOf(
                            orderItem.getCount()
                            )
                            .multiply(orderItem.getFood().getPrice()));

            orderItems.add(orderItem);
            orderItem.setOrder(newOrder);
            totalPrice = totalPrice.add(orderItem.getTotalPrice());
        }

        newOrder.setOrderItems(orderItems);
        newOrder.setTotalPrice(totalPrice);

        return orderMapper.toDto(orderRepository.save(newOrder));
    }

    @Override
    @Transactional
    public List<OrderDto> findByUserId(Long userId) {
        List<Order> orders = orderRepository.findByUserId(userId);
        updateOrdersList(orders);
        return orderMapper.toDtos(orders);
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
        List<Order> orders = orderRepository.findAll();
        updateOrdersList(orders);
        return orders
                .stream()
                .map(order -> orderInfoMapper.toDto(order, orderItemMapper)).toList();
    }

    @Override
    public List<OrderInfoDto> findByCountFood(int count) {
        List<Order> orders = orderRepository.findByCountFood(count);
        updateOrdersList(orders);
        return orders
                .stream()
                .map(order -> orderInfoMapper.toDto(order, orderItemMapper)).toList();
    }

    @Override
    public List<OrderInfoDto> findByFoodName(String foodName) {
        List<Order> orders = orderRepository.findByFoodName(foodName);
        updateOrdersList(orders);
        return orders
                .stream()
                .map(order -> orderInfoMapper.toDto(order, orderItemMapper)).toList();
    }

    @Override
    public OrderDto findById(Long id) {
        Order order = orderRepository
                .findById(id)
                .orElseThrow(() -> new EntityNotFoundException(ORDER_EX + id));
        if (order.getOrderItems().isEmpty()) {
            orderRepository.delete(order);
            throw new EntityNotFoundException(ORDER_EX + id);
        }
        BigDecimal totalPrice = BigDecimal.ZERO;

        for (OrderItem orderItem : order.getOrderItems()) {
            totalPrice = totalPrice.add(orderItem.getTotalPrice());
        }

        order.setTotalPrice(totalPrice);
        return orderMapper.toDto(order);
    }
}
