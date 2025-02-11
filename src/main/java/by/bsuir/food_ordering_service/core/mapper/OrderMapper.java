package by.bsuir.food_ordering_service.core.mapper;

import by.bsuir.food_ordering_service.api.dto.OrderDTO;
import by.bsuir.food_ordering_service.core.objects.Order;
import org.mapstruct.Mapper;

@Mapper(config = BaseMapper.class)
public interface OrderMapper extends BaseMapper<Order, OrderDTO> {
}
