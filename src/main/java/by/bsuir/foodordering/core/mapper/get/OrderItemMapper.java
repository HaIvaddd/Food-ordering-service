package by.bsuir.foodordering.core.mapper.get;

import by.bsuir.foodordering.api.dto.get.OrderItemDto;
import by.bsuir.foodordering.core.mapper.BaseMapper;
import by.bsuir.foodordering.core.models.OrderItem;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(config = BaseMapper.class, uses = {FoodMapper.class})
public interface OrderItemMapper extends BaseMapper<OrderItem, OrderItemDto> {
    @Mapping(target = "foodDto", source = "food", qualifiedByName = "toFoodDto")
    OrderItemDto toDto(OrderItem order);
}
