package by.bsuir.foodordering.core.repository;

import by.bsuir.foodordering.core.objects.Food;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.Getter;
import org.springframework.stereotype.Repository;



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

    public List<Food> findByType(String type) {
        return foods.values()
                .stream()
                .filter(food -> food.hasType(type))
                .toList();
    }

    public void save(Food food) {
        foods.put(food.getId(), food);
    }
}
