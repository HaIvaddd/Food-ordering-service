package by.bsuir.food_ordering_service.core.mapper;

import by.bsuir.food_ordering_service.api.dto.FoodDTO;
import by.bsuir.food_ordering_service.core.objects.Food;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(config = BaseMapper.class)
public interface FoodMapper extends BaseMapper<Food, FoodDTO> {
}
