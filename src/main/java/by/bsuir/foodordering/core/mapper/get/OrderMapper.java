package by.bsuir.foodordering.core.mapper.get;

import by.bsuir.foodordering.api.dto.get.OrderDto;
import by.bsuir.foodordering.core.mapper.BaseMapper;
import by.bsuir.foodordering.core.models.Order;
import by.bsuir.foodordering.core.models.OrderItem;
import java.util.List;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

@Mapper(config = BaseMapper.class)
public interface OrderMapper extends BaseMapper<Order, OrderDto> {

    @Mapping(target = "orderItemIds",
            source = "orderItems",
            qualifiedByName = "mapOrderItemsToOrderItemIds")
    @Mapping(target = "userId", source = "user.id")
    OrderDto toDto(Order order);


    @Named("mapOrderItemsToOrderItemIds")
    default List<Long> mapOrderItemsToOrderItemIds(List<OrderItem> orderItems) {
        return orderItems
                .stream()
                .map(OrderItem::getId)
                .toList();
    }


}