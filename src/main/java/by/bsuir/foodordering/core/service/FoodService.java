package by.bsuir.foodordering.core.service;

import by.bsuir.foodordering.api.dto.create.CreateFoodDto;
import by.bsuir.foodordering.api.dto.get.FoodDto;
import java.util.List;

public interface FoodService {

    List<FoodDto> findAll();

    List<FoodDto> findByType(String type);

    List<FoodDto> createBulk(List<CreateFoodDto> foodDtos);

    FoodDto findById(Long id);

    FoodDto findByName(String name);

    FoodDto create(CreateFoodDto foodDto);

    FoodDto update(FoodDto foodDto);

    void delete(Long foodId);
}
