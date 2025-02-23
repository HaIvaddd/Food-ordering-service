package by.bsuir.foodordering.api;

import by.bsuir.foodordering.api.dto.create.CreateFoodDto;
import by.bsuir.foodordering.api.dto.get.FoodDto;
import by.bsuir.foodordering.core.service.FoodService;
import java.util.List;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
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

    @PostMapping("/create")
    public FoodDto createFood(@RequestBody CreateFoodDto createFoodDto) {
        return foodService.create(createFoodDto);
    }

    @DeleteMapping("/del/{id}")
    public void deleteFood(@PathVariable("id") Long id) {
        foodService.delete(id);
    }

    @PutMapping("/update")
    public FoodDto updateFood(@RequestBody FoodDto foodDto) {
        return foodService.update(foodDto);
    }

    @GetMapping
    public List<FoodDto> findFoodAll() {
        return foodService.findAll();
    }
}
