package by.bsuir.foodordering.service;

import by.bsuir.foodordering.api.dto.create.CreateOrderDto;
import by.bsuir.foodordering.api.dto.create.CreateOrderItemDto;
import by.bsuir.foodordering.api.dto.get.OrderDto;
import by.bsuir.foodordering.api.dto.get.OrderInfoDto;
import by.bsuir.foodordering.core.cache.Cache;
import by.bsuir.foodordering.core.exception.CreatedEntityException;
import by.bsuir.foodordering.core.exception.EntityNotFoundException;
import by.bsuir.foodordering.core.exception.MakeOrderException;
import by.bsuir.foodordering.core.mapper.create.CreateOrderItemMapper;
import by.bsuir.foodordering.core.mapper.get.OrderInfoMapper;
import by.bsuir.foodordering.core.mapper.get.OrderItemMapper;
import by.bsuir.foodordering.core.mapper.get.OrderMapper;
import by.bsuir.foodordering.core.models.*;
import by.bsuir.foodordering.core.repository.FoodRepository;
import by.bsuir.foodordering.core.repository.OrderItemRepository;
import by.bsuir.foodordering.core.repository.OrderRepository;
import by.bsuir.foodordering.core.repository.UserRepository;
import by.bsuir.foodordering.core.service.impl.OrderHistoryServiceImpl;
import by.bsuir.foodordering.core.service.impl.OrderServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class OrderServiceTest {

    @Mock
    private OrderInfoMapper orderInfoMapper;
    @Mock
    private OrderMapper orderMapper;
    @Mock
    private FoodRepository foodRepository;
    @Mock
    private OrderRepository orderRepository;
    @Mock
    private OrderItemRepository orderItemRepository;
    @Mock
    private CreateOrderItemMapper createOrderItemMapper;
    @Mock
    private OrderItemMapper orderItemMapper;
    @Mock
    private UserRepository userRepository;
    @Mock
    private OrderHistoryServiceImpl orderHistoryService;
    @Mock
    private Cache foodCache;

    @InjectMocks
    private OrderServiceImpl orderService;

    @Captor
    private ArgumentCaptor<Order> orderCaptor;
    @Captor
    private ArgumentCaptor<Food> foodCaptor;

    private final Long userId = 1L;
    private final Long orderId = 10L;
    private final Long foodId1 = 101L;
    private final Long foodId2 = 102L;

    private User testUser;
    private Food testFood1;
    private Food testFood2;
    private Order testOrder;
    private OrderDto testOrderDto;
    private CreateOrderDto testCreateOrderDto;
    private CreateOrderItemDto testCreateItemDto1;
    private CreateOrderItemDto testCreateItemDto2;

    @BeforeEach
    void setUp() {
        testUser = new User();
        testUser.setId(userId);
        testUser.setName("Test User");

        testFood1 = new Food();
        testFood1.setId(foodId1);
        testFood1.setName("Pizza");
        testFood1.setPrice(BigDecimal.TEN);

        testFood2 = new Food();
        testFood2.setId(foodId2);
        testFood2.setName("Burger");
        testFood2.setPrice(BigDecimal.valueOf(8));

        Long orderItemId1 = 201L;
        OrderItem testOrderItem1 = new OrderItem(orderItemId1, null, testFood1, 2, BigDecimal.valueOf(20));
        Long orderItemId2 = 202L;
        OrderItem testOrderItem2 = new OrderItem(orderItemId2, null, testFood2, 1, BigDecimal.valueOf(8));

        testOrder = new Order();
        testOrder.setId(orderId);
        testOrder.setUser(testUser);
        testOrder.setCreatedAt(LocalDateTime.now().minusDays(1));
        testOrder.setOrdered(false);

        testOrderItem1.setOrder(testOrder);
        testOrderItem2.setOrder(testOrder);
        testOrder.getOrderItems().addAll(List.of(testOrderItem1, testOrderItem2));
        testOrder.setTotalPrice(testOrderItem1.getTotalPrice().add(testOrderItem2.getTotalPrice()));

        testOrderDto = new OrderDto();
        testOrderDto.setId(orderId);
        testOrderDto.setUserId(userId);
        testOrderDto.setOrdered(false);
        testOrderDto.setTotalPrice(testOrder.getTotalPrice());
        testOrderDto.setOrderItemIds(List.of(orderItemId1, orderItemId2));

        testCreateItemDto1 = new CreateOrderItemDto();
        testCreateItemDto1.setFoodId(foodId1);
        testCreateItemDto1.setCount(2);

        testCreateItemDto2 = new CreateOrderItemDto();
        testCreateItemDto2.setFoodId(foodId2);
        testCreateItemDto2.setCount(1);

        testCreateOrderDto = new CreateOrderDto();
        testCreateOrderDto.setUserId(userId);
        testCreateOrderDto.setCreateOrderItems(List.of(testCreateItemDto1, testCreateItemDto2));
    }

    @Test
    void makeOrderById_whenOrderExists_shouldSetOrderedCallHistoryAndReturnDto() {
        when(orderRepository.findById(orderId)).thenReturn(Optional.of(testOrder));
        doNothing().when(orderHistoryService).addOrderToHistory(any(Order.class));
        when(orderMapper.toDto(any(Order.class))).thenAnswer(invocation -> {
            Order capturedOrder = invocation.getArgument(0);
            assertTrue(capturedOrder.isOrdered());
            assertEquals(orderId, capturedOrder.getId());
            testOrderDto.setOrdered(true);
            return testOrderDto;
        });

        OrderDto result = orderService.makeOrderById(orderId);

        assertNotNull(result);
        assertTrue(result.isOrdered());
        assertEquals(orderId, result.getId());

        verify(orderRepository).findById(orderId);
        verify(orderHistoryService).addOrderToHistory(orderCaptor.capture());
        verify(orderMapper).toDto(testOrder);

        Order captured = orderCaptor.getValue();
        assertEquals(orderId, captured.getId());
        assertTrue(captured.isOrdered());
    }

    @Test
    void makeOrderById_whenOrderNotFound_shouldThrowEntityNotFoundException() {
        when(orderRepository.findById(orderId)).thenReturn(Optional.empty());
        assertThrows(EntityNotFoundException.class, () -> orderService.makeOrderById(orderId));
        verify(orderHistoryService, never()).addOrderToHistory(any());
        verify(orderMapper, never()).toDto(any());
    }

    @Test
    void create_whenValidDtoAndCacheMissAndHit_shouldCreateOrderAndReturnDto() {
        when(userRepository.findById(userId)).thenReturn(Optional.of(testUser));

        OrderItem itemEntity1 = new OrderItem(null, null, null, testCreateItemDto1.getCount(), null);
        OrderItem itemEntity2 = new OrderItem(null, null, null, testCreateItemDto2.getCount(), null);
        when(createOrderItemMapper.toEntity(testCreateItemDto1)).thenReturn(itemEntity1);
        when(createOrderItemMapper.toEntity(testCreateItemDto2)).thenReturn(itemEntity2);

        when(foodCache.get(foodId1)).thenReturn(null);
        when(foodRepository.findById(foodId1)).thenReturn(Optional.of(testFood1));
        when(foodCache.put(any(Food.class))).thenAnswer(invocation -> invocation.getArgument(0));

        when(foodCache.get(foodId2)).thenReturn(testFood2);

        when(orderRepository.save(any(Order.class))).thenAnswer(invocation -> {
            Order orderToSave = invocation.getArgument(0);
            orderToSave.setId(orderId);
            return orderToSave;
        });

        when(orderMapper.toDto(any(Order.class))).thenReturn(testOrderDto);

        OrderDto result = orderService.create(testCreateOrderDto);

        assertNotNull(result);
        assertEquals(testOrderDto, result, "The returned OrderDto should match the expected one.");

        verify(userRepository).findById(userId);
        verify(createOrderItemMapper).toEntity(testCreateItemDto1);
        verify(createOrderItemMapper).toEntity(testCreateItemDto2);
        verify(foodCache).get(foodId1);
        verify(foodRepository).findById(foodId1);
        verify(foodCache).put(foodCaptor.capture());
        verify(foodRepository, never()).findById(foodId2);
        verify(orderRepository).save(orderCaptor.capture());
        verify(orderMapper).toDto(orderCaptor.getValue());

        assertEquals(foodId1, foodCaptor.getValue().getId(), "Food put into cache should have the correct ID.");

        Order savedOrder = orderCaptor.getValue();
        assertNotNull(savedOrder.getUser(), "Saved order must have a user.");
        assertEquals(userId, savedOrder.getUser().getId(), "Saved order must have the correct user ID.");
        assertNotNull(savedOrder.getCreatedAt(), "Saved order must have a creation timestamp.");
        assertEquals(2, savedOrder.getOrderItems().size(), "Saved order should contain 2 items.");
        assertEquals(BigDecimal.valueOf(28), savedOrder.getTotalPrice(), "Total price in saved order is incorrect.");

        OrderItem savedItem1 = savedOrder.getOrderItems().stream()
                .filter(oi -> oi.getFood().getId().equals(foodId1)).findFirst()
                .orElseThrow(() -> new AssertionError("Saved order should contain item for foodId1"));
        OrderItem savedItem2 = savedOrder.getOrderItems().stream()
                .filter(oi -> oi.getFood().getId().equals(foodId2)).findFirst()
                .orElseThrow(() -> new AssertionError("Saved order should contain item for foodId2"));

        assertEquals(testFood1, savedItem1.getFood(), "Food reference in saved item 1 is incorrect.");
        assertEquals(BigDecimal.valueOf(20), savedItem1.getTotalPrice(), "Total price for saved item 1 is incorrect.");
        assertEquals(savedOrder, savedItem1.getOrder(), "Order reference in saved item 1 is incorrect.");

        assertEquals(testFood2, savedItem2.getFood(), "Food reference in saved item 2 is incorrect.");
        assertEquals(BigDecimal.valueOf(8), savedItem2.getTotalPrice(), "Total price for saved item 2 is incorrect.");
        assertEquals(savedOrder, savedItem2.getOrder(), "Order reference in saved item 2 is incorrect.");
    }

    @Test
    void create_whenDtoIsNull_shouldThrowCreatedEntityException() {
        assertThrows(CreatedEntityException.class, () -> orderService.create(null));
    }

    @Test
    void create_whenOrderItemsEmpty_shouldThrowCreatedEntityException() {
        testCreateOrderDto.setCreateOrderItems(Collections.emptyList());
        assertThrows(CreatedEntityException.class, () -> orderService.create(testCreateOrderDto));
    }

    @Test
    void create_whenUserNotFound_shouldThrowEntityNotFoundException() {
        when(userRepository.findById(userId)).thenReturn(Optional.empty());
        assertThrows(EntityNotFoundException.class, () -> orderService.create(testCreateOrderDto));
        verify(orderRepository, never()).save(any());
    }

    @Test
    void create_whenFoodNotFoundAndNotInCache_shouldThrowEntityNotFoundException() {
        when(userRepository.findById(userId)).thenReturn(Optional.of(testUser));
        when(createOrderItemMapper.toEntity(testCreateItemDto1)).thenReturn(new OrderItem(null, null, null, 2, null));
        when(foodCache.get(foodId1)).thenReturn(null);
        when(foodRepository.findById(foodId1)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> orderService.create(testCreateOrderDto));

        verify(foodCache).get(foodId1);
        verify(foodRepository).findById(foodId1);
        verify(foodCache, never()).put(any());
        verify(orderRepository, never()).save(any());
    }

    @Test
    void findByUserId_whenOrdersFound_shouldUpdateAndMapToDtos() {
        Order emptyOrder = new Order();
        emptyOrder.setId(99L);
        emptyOrder.setUser(testUser);
        emptyOrder.setOrderItems(new ArrayList<>());
        emptyOrder.setTotalPrice(BigDecimal.ZERO);

        List<Order> ordersFromRepo = new ArrayList<>(List.of(testOrder, emptyOrder));

        when(orderRepository.findByUserId(userId)).thenReturn(ordersFromRepo);

        doNothing().when(orderRepository).delete(emptyOrder);

        when(orderMapper.toDtos(anyList())).thenAnswer(invocation -> {
            List<Order> ordersToMap = invocation.getArgument(0);
            assertEquals(1, ordersToMap.size());
            assertTrue(ordersToMap.contains(testOrder));
            assertFalse(ordersToMap.contains(emptyOrder));
            return List.of(testOrderDto);
        });

        List<OrderDto> result = orderService.findByUserId(userId);

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(testOrderDto, result.getFirst());

        verify(orderRepository).findByUserId(userId);
        verify(orderRepository).delete(emptyOrder);
        verify(orderMapper).toDtos(argThat(list -> list.size() == 1 && list.contains(testOrder)));
    }

    @Test
    void findByUserId_whenNoOrdersFound_shouldReturnEmptyList() {
        when(orderRepository.findByUserId(userId)).thenReturn(Collections.emptyList());
        when(orderMapper.toDtos(Collections.emptyList())).thenReturn(Collections.emptyList());

        List<OrderDto> result = orderService.findByUserId(userId);

        assertTrue(result.isEmpty());
        verify(orderRepository).findByUserId(userId);
        verify(orderRepository, never()).delete(any());
        verify(orderMapper).toDtos(Collections.emptyList());
    }

    @Test
    void deleteById_whenOrderExists_shouldCallRepositoryDelete() {
        when(orderRepository.existsById(orderId)).thenReturn(true);
        doNothing().when(orderRepository).deleteById(orderId);

        orderService.deleteById(orderId);

        verify(orderRepository).existsById(orderId);
        verify(orderRepository).deleteById(orderId);
    }

    @Test
    void deleteById_whenOrderNotExists_shouldThrowEntityNotFoundException() {
        when(orderRepository.existsById(orderId)).thenReturn(false);

        assertThrows(EntityNotFoundException.class, () -> orderService.deleteById(orderId));

        verify(orderRepository).existsById(orderId);
        verify(orderRepository, never()).deleteById(anyLong());
    }

    @Test
    void findByCountFood_whenOrdersFound_shouldUpdateAndMapToInfoDtos() {
        int targetCount = 5;
        Order emptyOrder = new Order();
        emptyOrder.setId(99L);
        emptyOrder.setUser(testUser);
        emptyOrder.setOrderItems(new ArrayList<>());

        OrderInfoDto testOrderInfoDto = new OrderInfoDto();
        testOrderInfoDto.setId(testOrder.getId());
        testOrderInfoDto.setId(testOrder.getUser().getId());

        List<Order> ordersFromRepo = new ArrayList<>(List.of(testOrder, emptyOrder));

        when(orderRepository.findByCountFood(targetCount)).thenReturn(ordersFromRepo);

        doNothing().when(orderRepository).delete(emptyOrder);

        when(orderInfoMapper.toDto(eq(testOrder), any(OrderItemMapper.class))).thenReturn(testOrderInfoDto);

        List<OrderInfoDto> result = orderService.findByCountFood(targetCount);

        assertNotNull(result);
        assertEquals(1, result.size(), "Should return only the non-empty order's DTO");
        assertEquals(testOrderInfoDto, result.getFirst(), "The returned DTO should match the expected one");

        verify(orderRepository).findByCountFood(targetCount);
        verify(orderRepository).delete(emptyOrder);
        verify(orderInfoMapper).toDto(eq(testOrder), any(OrderItemMapper.class));
        verify(orderInfoMapper, never()).toDto(eq(emptyOrder), any(OrderItemMapper.class));
    }

    @Test
    void findByCountFood_whenNoOrdersFound_shouldReturnEmptyList() {
        int targetCount = 10;
        when(orderRepository.findByCountFood(targetCount)).thenReturn(Collections.emptyList());
        List<OrderInfoDto> result = orderService.findByCountFood(targetCount);

        assertNotNull(result);
        assertTrue(result.isEmpty(), "Result list should be empty when no orders are found");

        verify(orderRepository).findByCountFood(targetCount);
        verify(orderRepository, never()).delete(any(Order.class));
        verify(orderInfoMapper, never()).toDto(any(Order.class), any(OrderItemMapper.class));
    }

    @Test
    void findByFoodName_whenOrdersFound_shouldUpdateAndMapToInfoDtos() {
        String targetFoodName = "Pizza";
        Order emptyOrder = new Order();
        emptyOrder.setId(99L);
        emptyOrder.setUser(testUser);
        emptyOrder.setOrderItems(new ArrayList<>());

        OrderInfoDto testOrderInfoDto = new OrderInfoDto();
        testOrderInfoDto.setId(testOrder.getId());
        testOrderInfoDto.setId(testOrder.getUser().getId());

        List<Order> ordersFromRepo = new ArrayList<>(List.of(testOrder, emptyOrder));

        when(orderRepository.findByFoodName(targetFoodName)).thenReturn(ordersFromRepo);
        doNothing().when(orderRepository).delete(emptyOrder);

        when(orderInfoMapper.toDto(eq(testOrder), any(OrderItemMapper.class))).thenReturn(testOrderInfoDto);

        List<OrderInfoDto> result = orderService.findByFoodName(targetFoodName);

        assertNotNull(result);
        assertEquals(1, result.size(), "Should return only the non-empty order's DTO");
        assertEquals(testOrderInfoDto, result.getFirst(), "The returned DTO should match the expected one");

        verify(orderRepository).findByFoodName(targetFoodName);
        verify(orderRepository).delete(emptyOrder);
        verify(orderInfoMapper).toDto(eq(testOrder), any(OrderItemMapper.class));
        verify(orderInfoMapper, never()).toDto(eq(emptyOrder), any(OrderItemMapper.class));
    }

    @Test
    void findByFoodName_whenNoOrdersFound_shouldReturnEmptyList() {
        String targetFoodName = "NonExistentFood";
        when(orderRepository.findByFoodName(targetFoodName)).thenReturn(Collections.emptyList());

        List<OrderInfoDto> result = orderService.findByFoodName(targetFoodName);

        assertNotNull(result);
        assertTrue(result.isEmpty(), "Result list should be empty when no orders are found");

        verify(orderRepository).findByFoodName(targetFoodName);
        verify(orderRepository, never()).delete(any(Order.class));
        verify(orderInfoMapper, never()).toDto(any(Order.class), any(OrderItemMapper.class));
    }

    @Test
    void findAll_whenOrdersExist_shouldUpdateAndMapToInfoDtos() {
        Order emptyOrder = new Order(); emptyOrder.setId(99L); emptyOrder.setUser(testUser); emptyOrder.setOrderItems(new ArrayList<>());
        List<Order> ordersFromRepo = new ArrayList<>(List.of(testOrder, emptyOrder));

        OrderInfoDto orderInfoDto = new OrderInfoDto();
        orderInfoDto.setId(testOrder.getId());

        when(orderRepository.findAll()).thenReturn(ordersFromRepo);

        doNothing().when(orderRepository).delete(emptyOrder);
        when(orderInfoMapper.toDto(eq(testOrder), any(OrderItemMapper.class))).thenReturn(orderInfoDto);
        List<OrderInfoDto> result = orderService.findAll();
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(orderInfoDto, result.getFirst());

        verify(orderRepository).findAll();
        verify(orderRepository).delete(emptyOrder);

        verify(orderInfoMapper).toDto(eq(testOrder), any(OrderItemMapper.class));
        verify(orderInfoMapper, never()).toDto(eq(emptyOrder), any(OrderItemMapper.class));
    }

}