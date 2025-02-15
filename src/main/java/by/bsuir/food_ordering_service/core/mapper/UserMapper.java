package by.bsuir.food_ordering_service.core.mapper;

import by.bsuir.food_ordering_service.api.dto.UserDTO;
import by.bsuir.food_ordering_service.core.objects.Order;
import by.bsuir.food_ordering_service.core.objects.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.util.List;
import java.util.stream.Collectors;

@Mapper(config = BaseMapper.class)
public interface UserMapper extends BaseMapper<User, UserDTO> {

    @Mapping(target= "ordersId", source= "orders", qualifiedByName = "mapOrderToIdOrder")
    UserDTO toDTO(User entity);

    @Named("mapOrderToIdOrder")
    default List<Long> mapOrderToIdOrder(List<Order> orderList){
        return orderList.stream().map(Order::getId).collect(Collectors.toList());
    }
}
