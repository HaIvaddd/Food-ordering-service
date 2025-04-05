package by.bsuir.foodordering.core.cache;

import by.bsuir.foodordering.core.models.Food;
import java.util.LinkedHashMap;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class Cache {

    private final Map<Long, Food> foodCache;

    private final Logger logger = LoggerFactory.getLogger(Cache.class);

    public Cache(@Value("${cache.food.capacity:10}") int capacity) {
        this.foodCache = new LinkedHashMap<>(capacity, 0.75f, true) {
            @Override
            protected boolean removeEldestEntry(Map.Entry<Long, Food> eldest) {
                return size() > capacity;
            }
        };
    }

    public Food put(Food food) {
        logger.info("put food to cache: {}", food);
        foodCache.put(food.getId(), food);
        return food;
    }

    public void deleteFood(Long foodId) {
        logger.info("delete food from cache: {}", foodId);
        foodCache.remove(foodId);
    }

    public Food get(Long foodId) {
        Food food = foodCache.getOrDefault(foodId, null);
        if (food != null) {
            logger.info("get food from cache: {}", foodId);
        }
        return food;
    }

    public Food get(String foodName) {
        Food food = foodCache.values().stream()
                .filter(f -> f.getName().equals(foodName)).findFirst().orElse(null);
        if (food != null) {
            logger.info("get food from cache: {}", foodName);
        }
        return food;
    }

}
