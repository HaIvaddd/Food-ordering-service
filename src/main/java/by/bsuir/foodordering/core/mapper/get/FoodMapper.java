package by.bsuir.foodordering.core.mapper.get;

import by.bsuir.foodordering.api.dto.get.FoodDto;
import by.bsuir.foodordering.core.mapper.BaseMapper;
import by.bsuir.foodordering.core.objects.Food;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(config = BaseMapper.class)
public interface FoodMapper extends BaseMapper<Food, FoodDto> {
    @Mapping(target = "foodTypeStr", source = "foodType")
    FoodDto toDto(Food food);
}
