package by.bsuir.foodordering.core.service;

import by.bsuir.foodordering.api.dto.FoodDto;
import java.util.List;

public interface BaseFoodService {

    List<FoodDto> findAll();

    List<FoodDto> findByType(String type);
}
