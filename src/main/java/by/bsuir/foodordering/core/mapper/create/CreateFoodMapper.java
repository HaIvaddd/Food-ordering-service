package by.bsuir.foodordering.core.mapper.create;

import by.bsuir.foodordering.api.dto.create.CreateFoodDto;
import by.bsuir.foodordering.core.exception.FoodTypeException;
import by.bsuir.foodordering.core.mapper.BaseMapper;
import by.bsuir.foodordering.core.objects.Food;
import by.bsuir.foodordering.core.objects.FoodType;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

@Mapper(config = BaseMapper.class)
public interface CreateFoodMapper extends BaseMapper<Food, CreateFoodDto> {
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "foodType",
            source = "foodTypeStr",
            qualifiedByName = "toFoodTypeFromFoodTypeStrMap")
    Food toEntity(CreateFoodDto dto);

    @Named("toFoodTypeFromFoodTypeStrMap")
    default FoodType toFoodTypeFromFoodTypeStrMap(String foodTypeStr) { //Нужно
        try {
            return FoodType.valueOf(foodTypeStr.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new FoodTypeException("Unknown FoodType: " + foodTypeStr);
        }

    }
}
