package by.bsuir.foodordering.core.service.impl;

import by.bsuir.foodordering.core.models.Order;
import by.bsuir.foodordering.core.models.OrderHistoryItem;
import by.bsuir.foodordering.core.models.OrderItem;
import by.bsuir.foodordering.core.repository.OrderHistoryRepository;
import by.bsuir.foodordering.core.service.OrderHistoryService;
import java.util.List;
import lombok.AllArgsConstructor;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class OrderHistoryServiceImpl implements OrderHistoryService {

    private final OrderHistoryRepository orderHistoryRepository;

    @Override
    public void removeOrderFromHistory(Long id) {
        orderHistoryRepository.deleteById(id);
    }

    @Override
    public List<OrderHistoryItem> findOrderHistoryByUserId(Long userId) {
        return orderHistoryRepository.findByUserId(userId);
    }

    @Override
    public List<OrderHistoryItem> findAll() {
        return orderHistoryRepository.findAll();
    }

    @Override
    public void addOrderToHistory(Order order) {
        JSONArray jsonArray = new JSONArray();
        JSONObject jsonObject;

        for (OrderItem orderItem : order.getOrderItems()) {
            jsonObject = new JSONObject();
            jsonObject.put("food_id", orderItem.getFood().getId());
            jsonObject.put("food_name", orderItem.getFood().getName());
            jsonObject.put("count", orderItem.getCount());
            jsonObject.put("price", orderItem.getTotalPrice());
            jsonArray.put(jsonObject);
        }

        OrderHistoryItem orderHistoryItem = new OrderHistoryItem();
        orderHistoryItem.setUserId(order.getUser().getId());
        orderHistoryItem.setCreatedAt(order.getCreatedAt());
        orderHistoryItem.setFoodItemsJsonArray(jsonArray);
        orderHistoryItem.setTotalPrice(order.getTotalPrice());
        orderHistoryRepository.save(orderHistoryItem);
    }
}
