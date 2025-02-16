package by.bsuir.foodordering.core.mapper;

import by.bsuir.foodordering.api.dto.OrderDto;
import by.bsuir.foodordering.core.objects.Order;
import by.bsuir.foodordering.core.objects.OrderItem;
import by.bsuir.foodordering.core.objects.User;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-02-16T22:45:04+0300",
    comments = "version: 1.6.3, compiler: IncrementalProcessingEnvironment from gradle-language-java-8.12.1.jar, environment: Java 23.0.2 (Oracle Corporation)"
)
@Component
public class OrderMapperImpl implements OrderMapper {

    @Override
    public Order toEntity(OrderDto dto) {
        if ( dto == null ) {
            return null;
        }

        Long id = null;
        LocalDateTime createdAt = null;

        id = dto.getId();
        createdAt = dto.getCreatedAt();

        User user = null;
        List<OrderItem> orderItems = null;

        Order order = new Order( id, user, createdAt, orderItems );

        return order;
    }

    @Override
    public List<OrderDto> toDtos(List<Order> entities) {
        if ( entities == null ) {
            return new ArrayList<OrderDto>();
        }

        List<OrderDto> list = new ArrayList<OrderDto>( entities.size() );
        for ( Order order : entities ) {
            list.add( toDto( order ) );
        }

        return list;
    }

    @Override
    public Order merge(Order entity, OrderDto dto) {
        if ( dto == null ) {
            return entity;
        }

        if ( dto.getId() != null ) {
            entity.setId( dto.getId() );
        }
        if ( dto.getCreatedAt() != null ) {
            entity.setCreatedAt( dto.getCreatedAt() );
        }

        return entity;
    }

    @Override
    public OrderDto toDto(Order order) {
        if ( order == null ) {
            return null;
        }

        OrderDto orderDto = new OrderDto();

        orderDto.setOrderItemIds( mapOrderItemsToOrderItemIds( order.getOrderItems() ) );
        orderDto.setUserId( orderUserId( order ) );
        orderDto.setId( order.getId() );
        orderDto.setCreatedAt( order.getCreatedAt() );

        return orderDto;
    }

    private Long orderUserId(Order order) {
        User user = order.getUser();
        if ( user == null ) {
            return null;
        }
        return user.getId();
    }
}
