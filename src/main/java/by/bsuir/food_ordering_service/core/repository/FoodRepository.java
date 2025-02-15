package by.bsuir.food_ordering_service.core.repository;

import by.bsuir.food_ordering_service.core.objects.Food;
import lombok.Getter;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
@Getter
public class FoodRepository {

    private final Map<Long, Food> foods = new HashMap<>();

    public List<Food> findAll() {
        return new ArrayList<>(foods.values());
    }

    public Food findById(Long id) {
        return foods.get(id);
    }

    public void save(Food food) {
        foods.put(food.getId(), food);
    }
}
