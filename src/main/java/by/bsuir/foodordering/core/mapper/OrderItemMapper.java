package by.bsuir.foodordering.core.mapper;

import by.bsuir.foodordering.api.dto.OrderItemDto;
import by.bsuir.foodordering.core.objects.OrderItem;
import org.mapstruct.Mapper;

@Mapper(config = BaseMapper.class)
public interface OrderItemMapper extends BaseMapper<OrderItem, OrderItemDto> {
}
