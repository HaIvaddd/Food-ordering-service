package by.bsuir.foodordering.core.mapper;

import by.bsuir.foodordering.api.dto.UserDto;
import by.bsuir.foodordering.core.objects.Order;
import by.bsuir.foodordering.core.objects.User;
import java.util.List;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

@Mapper(config = BaseMapper.class)
public interface UserMapper extends BaseMapper<User, UserDto> {

    @Mapping(target = "ordersId", source = "orders", qualifiedByName = "mapOrderToIdOrder")
    UserDto toDto(User entity);

    @Named("mapOrderToIdOrder")
    default List<Long> mapOrderToIdOrder(List<Order> orderList) {
        return orderList
                .stream()
                .map(Order::getId)
                .toList();
    }
}
