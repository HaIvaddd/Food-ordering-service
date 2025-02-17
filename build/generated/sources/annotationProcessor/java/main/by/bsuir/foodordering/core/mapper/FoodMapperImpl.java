package by.bsuir.foodordering.core.mapper;

import by.bsuir.foodordering.api.dto.FoodDto;
import by.bsuir.foodordering.core.objects.Food;
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
public class FoodMapperImpl implements FoodMapper {

    @Override
    public Food toEntity(FoodDto dto) {
        if ( dto == null ) {
            return null;
        }

        Food food = new Food();

        food.setId( dto.getId() );
        food.setName( dto.getName() );
        food.setPrice( dto.getPrice() );

        return food;
    }

    @Override
    public List<FoodDto> toDtos(List<Food> entities) {
        if ( entities == null ) {
            return new ArrayList<FoodDto>();
        }

        List<FoodDto> list = new ArrayList<FoodDto>( entities.size() );
        for ( Food food : entities ) {
            list.add( toDto( food ) );
        }

        return list;
    }

    @Override
    public Food merge(Food entity, FoodDto dto) {
        if ( dto == null ) {
            return entity;
        }

        if ( dto.getId() != null ) {
            entity.setId( dto.getId() );
        }
        if ( dto.getName() != null ) {
            entity.setName( dto.getName() );
        }
        if ( dto.getPrice() != null ) {
            entity.setPrice( dto.getPrice() );
        }

        return entity;
    }

    @Override
    public FoodDto toDto(Food food) {
        if ( food == null ) {
            return null;
        }

        FoodDto foodDto = new FoodDto();

        if ( food.getFoodType() != null ) {
            foodDto.setFoodTypeStr( food.getFoodType().name() );
        }
        foodDto.setId( food.getId() );
        foodDto.setName( food.getName() );
        foodDto.setPrice( food.getPrice() );

        return foodDto;
    }
}
