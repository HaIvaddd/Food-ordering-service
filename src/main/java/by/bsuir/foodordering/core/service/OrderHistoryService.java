package by.bsuir.foodordering.core.service;

import by.bsuir.foodordering.core.models.Order;
import by.bsuir.foodordering.core.models.OrderHistoryItem;
import java.util.List;

public interface OrderHistoryService {
    void addOrderToHistory(Order order);

    void removeOrderFromHistory(Long id);

    List<OrderHistoryItem> findOrderHistoryByUserId(Long userId);

    List<OrderHistoryItem> findAll();
}
