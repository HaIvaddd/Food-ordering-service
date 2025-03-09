package by.bsuir.foodordering.core.mapper.create;

import by.bsuir.foodordering.api.dto.create.CreateOrderItemDto;
import by.bsuir.foodordering.core.exception.EntityNotFoundException;
import by.bsuir.foodordering.core.mapper.BaseMapper;
import by.bsuir.foodordering.core.models.Food;
import by.bsuir.foodordering.core.models.OrderItem;
import by.bsuir.foodordering.core.repository.FoodRepository;
import org.mapstruct.Context;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

@Mapper(config = BaseMapper.class)
public interface CreateOrderItemMapper extends BaseMapper<OrderItem, CreateOrderItemDto> {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "order", ignore = true)
    @Mapping(target = "totalPrice", ignore = true)
    @Mapping(target = "food", source = "foodId", qualifiedByName = "toFoodFromFoodIdMap")
    OrderItem toEntity(CreateOrderItemDto createOrderItemDto,
                       @Context FoodRepository foodRepository);

    @Named("toFoodFromFoodIdMap")
    default Food toFoodFromFoodIdMap(Long foodId, @Context FoodRepository foodRepository) {
        //добавить обработку foodId на null в сервисе
        return foodRepository.findById(foodId)
                .orElseThrow(
                        () -> new EntityNotFoundException("Food not found with id: " + foodId)
                );
    }

}
