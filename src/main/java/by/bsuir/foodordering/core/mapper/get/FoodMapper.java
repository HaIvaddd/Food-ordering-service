package by.bsuir.foodordering.core.mapper.get;

import by.bsuir.foodordering.api.dto.get.FoodDto;
import by.bsuir.foodordering.core.mapper.BaseMapper;
import by.bsuir.foodordering.core.models.Food;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

@Mapper(config = BaseMapper.class)
public interface FoodMapper extends BaseMapper<Food, FoodDto> {
    @Named("toFoodDto")
    @Mapping(target = "foodTypeStr", source = "foodType")
    FoodDto toDto(Food food);
}
