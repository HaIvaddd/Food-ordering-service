package by.bsuir.foodordering.service;

import by.bsuir.foodordering.core.exception.EntityNotFoundException;
import by.bsuir.foodordering.core.models.*; // Импортируем все модели
import by.bsuir.foodordering.core.repository.OrderHistoryRepository;
import by.bsuir.foodordering.core.service.impl.OrderHistoryServiceImpl;
import org.json.JSONArray;
import org.json.JSONObject;
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
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class OrderHistoryServiceTest {

    @Mock
    private OrderHistoryRepository orderHistoryRepository;

    @InjectMocks
    private OrderHistoryServiceImpl orderHistoryService;

    @Captor
    private ArgumentCaptor<OrderHistoryItem> historyItemCaptor;

    private final Long userId = 1L;
    private final Long orderHistoryItemId = 10L;
    private Order testOrder;
    private OrderItem testOrderItem1;
    private OrderItem testOrderItem2;
    private OrderHistoryItem testHistoryItem;

    @BeforeEach
    void setUp() {
        User testUser = new User();
        testUser.setId(userId);
        testUser.setName("Test User");

        Food testFood1 = new Food();
        testFood1.setId(101L);
        testFood1.setName("Pizza");
        testFood1.setPrice(BigDecimal.TEN);

        Food testFood2 = new Food();
        testFood2.setId(102L);
        testFood2.setName("Burger");
        testFood2.setPrice(BigDecimal.valueOf(8));

        testOrderItem1 = new OrderItem(201L, null, testFood1, 2, BigDecimal.valueOf(20));
        testOrderItem2 = new OrderItem(202L, null, testFood2, 1, BigDecimal.valueOf(8));

        testOrder = new Order();
        testOrder.setId(50L);
        testOrder.setUser(testUser);
        testOrder.setCreatedAt(LocalDateTime.now().minusHours(1));
        testOrder.getOrderItems().add(testOrderItem1);
        testOrder.getOrderItems().add(testOrderItem2);
        testOrder.setTotalPrice(BigDecimal.valueOf(28));
        testOrderItem1.setOrder(testOrder);
        testOrderItem2.setOrder(testOrder);

        testHistoryItem = new OrderHistoryItem();
        testHistoryItem.setId(orderHistoryItemId);
        testHistoryItem.setUserId(userId);
        testHistoryItem.setCreatedAt(LocalDateTime.now().minusDays(1));
        testHistoryItem.setTotalPrice(BigDecimal.valueOf(50));

        JSONArray sampleJsonArray = new JSONArray();
        JSONObject sampleJsonObject = new JSONObject();
        sampleJsonObject.put("food_id", 100L);
        sampleJsonObject.put("food_name", "Sample Food");
        sampleJsonObject.put("count", 1);
        sampleJsonObject.put("price", 50.0);
        sampleJsonArray.put(sampleJsonObject);
        testHistoryItem.setFoodItemsJsonArray(sampleJsonArray);

    }

    @Test
    void removeOrderFromHistory_whenItemExists_shouldDelete() {
        when(orderHistoryRepository.existsById(orderHistoryItemId)).thenReturn(true);
        doNothing().when(orderHistoryRepository).deleteById(orderHistoryItemId);

        orderHistoryService.removeOrderFromHistory(orderHistoryItemId);

        verify(orderHistoryRepository).existsById(orderHistoryItemId);
        verify(orderHistoryRepository).deleteById(orderHistoryItemId);
    }

    @Test
    void removeOrderFromHistory_whenItemNotFound_shouldThrowEntityNotFoundException() {
        when(orderHistoryRepository.existsById(orderHistoryItemId)).thenReturn(false);

        EntityNotFoundException exception = assertThrows(
                EntityNotFoundException.class,
                () -> orderHistoryService.removeOrderFromHistory(orderHistoryItemId)
        );
        assertTrue(exception.getMessage().contains(String.valueOf(orderHistoryItemId)));

        verify(orderHistoryRepository).existsById(orderHistoryItemId);
        verify(orderHistoryRepository, never()).deleteById(anyLong());
    }

    @Test
    void findOrderHistoryByUserId_whenItemsFound_shouldReturnList() {
        List<OrderHistoryItem> expectedList = List.of(testHistoryItem);
        when(orderHistoryRepository.findByUserId(userId)).thenReturn(expectedList);

        List<OrderHistoryItem> result = orderHistoryService.findOrderHistoryByUserId(userId);

        assertNotNull(result);
        assertEquals(expectedList, result);
        assertEquals(1, result.size());

        verify(orderHistoryRepository).findByUserId(userId);
    }

    @Test
    void findOrderHistoryByUserId_whenNoItemsFound_shouldReturnEmptyList() {
        when(orderHistoryRepository.findByUserId(userId)).thenReturn(Collections.emptyList());

        List<OrderHistoryItem> result = orderHistoryService.findOrderHistoryByUserId(userId);

        assertNotNull(result);
        assertTrue(result.isEmpty());

        verify(orderHistoryRepository).findByUserId(userId);
    }

    @Test
    void findAll_whenItemsExist_shouldReturnList() {
        OrderHistoryItem anotherItem = new OrderHistoryItem();
        anotherItem.setId(orderHistoryItemId + 1);
        anotherItem.setUserId(userId + 1);
        List<OrderHistoryItem> expectedList = List.of(testHistoryItem, anotherItem);
        when(orderHistoryRepository.findAll()).thenReturn(expectedList);

        List<OrderHistoryItem> result = orderHistoryService.findAll();

        assertNotNull(result);
        assertEquals(expectedList, result);
        assertEquals(2, result.size());

        verify(orderHistoryRepository).findAll();
    }

    @Test
    void findAll_whenNoItemsExist_shouldReturnEmptyList() {
        when(orderHistoryRepository.findAll()).thenReturn(Collections.emptyList());

        List<OrderHistoryItem> result = orderHistoryService.findAll();

        assertNotNull(result);
        assertTrue(result.isEmpty());

        verify(orderHistoryRepository).findAll();
    }

    @Test
    void addOrderToHistory_shouldCreateCorrectHistoryItemAndSave() {
        when(orderHistoryRepository.save(any(OrderHistoryItem.class))).thenAnswer(invocation -> invocation.getArgument(0));

        orderHistoryService.addOrderToHistory(testOrder);

        verify(orderHistoryRepository).save(historyItemCaptor.capture());
        OrderHistoryItem capturedItem = historyItemCaptor.getValue();

        assertNotNull(capturedItem);
        assertEquals(testOrder.getUser().getId(), capturedItem.getUserId());
        assertEquals(testOrder.getCreatedAt(), capturedItem.getCreatedAt());
        assertEquals(testOrder.getTotalPrice(), capturedItem.getTotalPrice());
        assertNull(capturedItem.getId());

        assertNotNull(capturedItem.getFoodItemsJson());
        JSONArray parsedJsonArray = new JSONArray(capturedItem.getFoodItemsJson());
        assertEquals(2, parsedJsonArray.length(), "JSON array should contain 2 items");

        JSONObject jsonItem1 = parsedJsonArray.getJSONObject(0); // Порядок может быть важен!
        assertEquals(testOrderItem1.getFood().getId(), jsonItem1.getLong("food_id"));
        assertEquals(testOrderItem1.getFood().getName(), jsonItem1.getString("food_name"));
        assertEquals(testOrderItem1.getCount(), jsonItem1.getInt("count"));

        assertEquals(testOrderItem1.getTotalPrice().doubleValue(), jsonItem1.getBigDecimal("price").doubleValue());

        JSONObject jsonItem2 = parsedJsonArray.getJSONObject(1);
        assertEquals(testOrderItem2.getFood().getId(), jsonItem2.getLong("food_id"));
        assertEquals(testOrderItem2.getFood().getName(), jsonItem2.getString("food_name"));
        assertEquals(testOrderItem2.getCount(), jsonItem2.getInt("count"));
        assertEquals(testOrderItem2.getTotalPrice().doubleValue(), jsonItem2.getBigDecimal("price").doubleValue());
    }

    @Test
    void addOrderToHistory_whenOrderHasNoItems_shouldSaveWithEmptyJsonArray() {
        testOrder.getOrderItems().clear();
        testOrder.setTotalPrice(BigDecimal.ZERO);
        when(orderHistoryRepository.save(any(OrderHistoryItem.class))).thenAnswer(invocation -> invocation.getArgument(0));

        orderHistoryService.addOrderToHistory(testOrder);

        verify(orderHistoryRepository).save(historyItemCaptor.capture());
        OrderHistoryItem capturedItem = historyItemCaptor.getValue();

        assertNotNull(capturedItem);
        assertEquals(testOrder.getUser().getId(), capturedItem.getUserId());
        assertEquals(testOrder.getCreatedAt(), capturedItem.getCreatedAt());
        assertEquals(BigDecimal.ZERO, capturedItem.getTotalPrice());

        assertNotNull(capturedItem.getFoodItemsJson());
        assertEquals("[]", capturedItem.getFoodItemsJson());

        JSONArray parsedJsonArray = new JSONArray(capturedItem.getFoodItemsJson());
        assertEquals(0, parsedJsonArray.length());
    }
}