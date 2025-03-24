package by.bsuir.foodordering.core.mapper.create;

import by.bsuir.foodordering.api.dto.create.CreateOrderItemDto;
import by.bsuir.foodordering.core.mapper.BaseMapper;
import by.bsuir.foodordering.core.models.OrderItem;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(config = BaseMapper.class)
public interface CreateOrderItemMapper extends BaseMapper<OrderItem, CreateOrderItemDto> {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "order", ignore = true)
    @Mapping(target = "totalPrice", ignore = true)
    @Mapping(target = "food", ignore = true)
    OrderItem toEntity(CreateOrderItemDto createOrderItemDto);

}
