package by.bsuir.food_ordering_service.core.mapper;

import by.bsuir.food_ordering_service.api.dto.OrderItemDTO;
import by.bsuir.food_ordering_service.core.objects.OrderItem;
import org.mapstruct.Mapper;

@Mapper(config = BaseMapper.class)
public interface OrderItemMapper extends BaseMapper<OrderItem, OrderItemDTO> {
}
