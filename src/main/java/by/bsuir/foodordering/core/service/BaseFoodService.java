package by.bsuir.foodordering.core.service;

import by.bsuir.foodordering.api.dto.create.CreateFoodDto;
import by.bsuir.foodordering.api.dto.get.FoodDto;
import java.util.List;

public interface BaseFoodService {

    List<FoodDto> findAll();

    List<FoodDto> findByType(String type);

    FoodDto findById(Long id);

    FoodDto create(CreateFoodDto foodDto);

    FoodDto update(FoodDto foodDto);

    void delete(Long foodId);
}
