package by.bsuir.foodordering.core.mapper.create;

import by.bsuir.foodordering.api.dto.create.CreateFoodDto;
import by.bsuir.foodordering.core.exception.FoodTypeException;
import by.bsuir.foodordering.core.mapper.BaseMapper;
import by.bsuir.foodordering.core.models.Food;
import by.bsuir.foodordering.core.models.FoodType;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Mapper(config = BaseMapper.class)
public interface CreateFoodMapper extends BaseMapper<Food, CreateFoodDto> {
    Logger logger = LoggerFactory.getLogger(CreateFoodMapper.class);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "foodType",
            source = "foodTypeStr",
            qualifiedByName = "toFoodTypeFromFoodTypeStrMap")
    Food toEntity(CreateFoodDto dto);

    @Named("toFoodTypeFromFoodTypeStrMap")
    default FoodType toFoodTypeFromFoodTypeStrMap(String foodTypeStr) {
        try {
            FoodType foodType = FoodType.valueOf(foodTypeStr.toUpperCase());
            logger.info("Mapped FoodType: {}", foodType); // Логирование возвращаемого значения
            return foodType;
        } catch (IllegalArgumentException e) {
            logger.error("Failed to map FoodType for: {}", foodTypeStr, e); // Логирование ошибки
            throw new FoodTypeException("Unknown FoodType: " + foodTypeStr);
        }
    }
}
