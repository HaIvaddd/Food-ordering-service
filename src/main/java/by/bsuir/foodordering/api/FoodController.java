package by.bsuir.foodordering.api;

import by.bsuir.foodordering.api.dto.create.CreateFoodDto;
import by.bsuir.foodordering.api.dto.get.FoodDto;
import by.bsuir.foodordering.core.service.impl.FoodServiceImpl;
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

    private final FoodServiceImpl foodServiceImpl;

    @GetMapping(params = "type")
    public List<FoodDto> findFoodByType(@RequestParam("type") String foodType) {
        return foodServiceImpl.findByType(foodType);
    }

    @PostMapping("/create")
    public FoodDto createFood(@RequestBody CreateFoodDto createFoodDto) {
        return foodServiceImpl.create(createFoodDto);
    }

    @DeleteMapping("/del/{id}")
    public void deleteFood(@PathVariable("id") Long id) {
        foodServiceImpl.delete(id);
    }

    @PutMapping("/update")
    public FoodDto updateFood(@RequestBody FoodDto foodDto) {
        return foodServiceImpl.update(foodDto);
    }

    @GetMapping
    public List<FoodDto> findFoodAll() {
        return foodServiceImpl.findAll();
    }
}
