package by.bsuir.foodordering.core.mapper.get;

import by.bsuir.foodordering.api.dto.get.OrderInfoDto;
import by.bsuir.foodordering.api.dto.get.OrderItemDto;
import by.bsuir.foodordering.core.mapper.BaseMapper;
import by.bsuir.foodordering.core.models.Order;
import by.bsuir.foodordering.core.models.OrderItem;
import java.util.List;
import org.mapstruct.Context;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

@Mapper(config = BaseMapper.class, uses = {UserMapper.class})
public interface OrderInfoMapper extends BaseMapper<Order, OrderInfoDto> {

    @Mapping(target = "orderItemDtos",
            source = "orderItems",
            qualifiedByName = "mapOrderItemsToOrderItemDto")
    @Mapping(target = "userDto", source = "user", qualifiedByName = "toUserDto")
    OrderInfoDto toDto(Order order, @Context OrderItemMapper orderItemMapper);


    @Named("mapOrderItemsToOrderItemDto")
    default List<OrderItemDto> mapOrderItemsToOrderItemDto(
            List<OrderItem> orderItems,
            @Context OrderItemMapper orderItemMapper) {
        return orderItems
                .stream()
                .map(orderItemMapper::toDto)
                .toList();
    }
}