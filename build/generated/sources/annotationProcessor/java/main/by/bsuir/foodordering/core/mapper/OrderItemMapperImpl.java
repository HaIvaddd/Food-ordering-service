package by.bsuir.foodordering.core.mapper;

import by.bsuir.foodordering.api.dto.OrderItemDto;
import by.bsuir.foodordering.core.objects.Food;
import by.bsuir.foodordering.core.objects.Order;
import by.bsuir.foodordering.core.objects.OrderItem;
import java.math.BigDecimal;
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
public class OrderItemMapperImpl implements OrderItemMapper {

    @Override
    public OrderItemDto toDto(OrderItem entity) {
        if ( entity == null ) {
            return null;
        }

        OrderItemDto orderItemDto = new OrderItemDto();

        return orderItemDto;
    }

    @Override
    public OrderItem toEntity(OrderItemDto dto) {
        if ( dto == null ) {
            return null;
        }

        Long id = null;
        Order order = null;
        Food food = null;
        Integer count = null;
        BigDecimal totalPrice = null;

        OrderItem orderItem = new OrderItem( id, order, food, count, totalPrice );

        return orderItem;
    }

    @Override
    public List<OrderItemDto> toDtos(List<OrderItem> entities) {
        if ( entities == null ) {
            return new ArrayList<OrderItemDto>();
        }

        List<OrderItemDto> list = new ArrayList<OrderItemDto>( entities.size() );
        for ( OrderItem orderItem : entities ) {
            list.add( toDto( orderItem ) );
        }

        return list;
    }

    @Override
    public OrderItem merge(OrderItem entity, OrderItemDto dto) {
        if ( dto == null ) {
            return entity;
        }

        return entity;
    }
}
