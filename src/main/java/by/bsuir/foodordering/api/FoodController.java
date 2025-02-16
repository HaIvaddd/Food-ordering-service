package by.bsuir.foodordering.api;

import by.bsuir.foodordering.api.dto.FoodDto;
import by.bsuir.foodordering.core.service.FoodService;
import java.util.List;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/food")
@AllArgsConstructor
public class FoodController {

    private final FoodService foodService;

    @GetMapping(params = "foodType")
    public List<FoodDto> findFoodByType(@RequestParam("foodType") String foodType) {
        return foodService.findByType(foodType);
    }

}
