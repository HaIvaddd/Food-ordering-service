package by.bsuir.foodordering.core.mapper;

import by.bsuir.foodordering.api.dto.FoodDto;
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
    date = "2025-02-17T09:19:55+0300",
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

        orderItemDto.setFood( foodToFoodDto( entity.getFood() ) );
        orderItemDto.setCount( entity.getCount() );
        orderItemDto.setTotalPrice( entity.getTotalPrice() );

        return orderItemDto;
    }

    @Override
    public OrderItem toEntity(OrderItemDto dto) {
        if ( dto == null ) {
            return null;
        }

        Food food = null;
        Integer count = null;
        BigDecimal totalPrice = null;

        food = foodDtoToFood( dto.getFood() );
        count = dto.getCount();
        totalPrice = dto.getTotalPrice();

        Long id = null;
        Order order = null;

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

        if ( dto.getFood() != null ) {
            if ( entity.getFood() == null ) {
                entity.setFood( new Food() );
            }
            foodDtoToFood1( dto.getFood(), entity.getFood() );
        }
        if ( dto.getCount() != null ) {
            entity.setCount( dto.getCount() );
        }
        if ( dto.getTotalPrice() != null ) {
            entity.setTotalPrice( dto.getTotalPrice() );
        }

        return entity;
    }

    protected FoodDto foodToFoodDto(Food food) {
        if ( food == null ) {
            return null;
        }

        FoodDto foodDto = new FoodDto();

        foodDto.setId( food.getId() );
        foodDto.setName( food.getName() );
        foodDto.setPrice( food.getPrice() );

        return foodDto;
    }

    protected Food foodDtoToFood(FoodDto foodDto) {
        if ( foodDto == null ) {
            return null;
        }

        Food food = new Food();

        food.setId( foodDto.getId() );
        food.setName( foodDto.getName() );
        food.setPrice( foodDto.getPrice() );

        return food;
    }

    protected void foodDtoToFood1(FoodDto foodDto, Food mappingTarget) {
        if ( foodDto == null ) {
            return;
        }

        if ( foodDto.getId() != null ) {
            mappingTarget.setId( foodDto.getId() );
        }
        if ( foodDto.getName() != null ) {
            mappingTarget.setName( foodDto.getName() );
        }
        if ( foodDto.getPrice() != null ) {
            mappingTarget.setPrice( foodDto.getPrice() );
        }
    }
}
