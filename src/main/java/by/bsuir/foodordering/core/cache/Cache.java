package by.bsuir.foodordering.core.cache;

import by.bsuir.foodordering.core.models.Food;
import java.util.LinkedHashMap;
import java.util.Map;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class Cache {

    private final Map<Long, Food> foodCache;

    public Cache(@Value("${cache.food.capacity:10}") int capacity) {
        this.foodCache = new LinkedHashMap<>(capacity, 0.75f, true) {
            @Override
            protected boolean removeEldestEntry(Map.Entry<Long, Food> eldest) {
                return size() > capacity;
            }
        };
    }

    public Food put(Food food) {
        return foodCache.put(food.getId(), food);
    }

    public void deleteFood(Long foodId) {
        foodCache.remove(foodId);
    }

    public Food get(Long foodId) {
        return foodCache.getOrDefault(foodId, null);
    }

}
