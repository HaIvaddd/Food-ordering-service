package by.bsuir.food_ordering_service.core.mapper;

import by.bsuir.food_ordering_service.api.dto.OrderDTO;
import by.bsuir.food_ordering_service.core.objects.Order;
import by.bsuir.food_ordering_service.core.objects.OrderItem;
import by.bsuir.food_ordering_service.core.objects.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.util.List;
import java.util.stream.Collectors;

@Mapper(config = BaseMapper.class)
public interface OrderMapper extends BaseMapper<Order, OrderDTO> {

    @Mapping(target= "orderItemIds", source= "orderItems", qualifiedByName = "mapOrderItemsToOrderItemIds")
    @Mapping(target= "userId", source= "user.id")
    OrderDTO toDTO(Order order);


    @Named("mapOrderItemsToOrderItemIds")
    default List<Long> mapOrderItemsToOrderItemIds(List<OrderItem> orderItems){
        return orderItems.stream().map(OrderItem::getId).collect(Collectors.toList());
    }
}
