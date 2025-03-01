package by.bsuir.foodordering.core.mapper.get;

import by.bsuir.foodordering.api.dto.get.OrderItemDto;
import by.bsuir.foodordering.core.mapper.BaseMapper;
import by.bsuir.foodordering.core.models.OrderItem;
import org.mapstruct.Mapper;

@Mapper(config = BaseMapper.class)
public interface OrderItemMapper extends BaseMapper<OrderItem, OrderItemDto> {
}
