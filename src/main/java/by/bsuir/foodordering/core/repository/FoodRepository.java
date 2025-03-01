package by.bsuir.foodordering.core.repository;

import by.bsuir.foodordering.core.models.Food;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import lombok.Getter;
import org.springframework.stereotype.Repository;

@Repository
@Getter
public class FoodRepository {

    private final Map<Long, Food> foods = new HashMap<>();

    public List<Food> findAll() {
        return new ArrayList<>(foods.values());
    }

    public Optional<Food> findById(Long id) {
        return  Optional.ofNullable(foods.get(id));
    }

    public List<Food> findByType(String type) {
        return foods.values()
                .stream()
                .filter(food -> food.hasType(type))
                .toList();
    }

    public Food saveFood(Food food) {
        return foods.put(food.getId(), food);
    }

    public void deleteById(Long foodId) {
        foods.remove(foodId);
    }

    public boolean existsById(Long foodId) {
        return foods.containsKey(foodId);
    }
}
