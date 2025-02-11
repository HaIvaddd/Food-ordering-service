package by.bsuir.food_ordering_service.core.mapper;

import by.bsuir.food_ordering_service.api.dto.food.PizzaDTO;
import by.bsuir.food_ordering_service.core.objects.food.Pizza;
import org.mapstruct.Mapper;

@Mapper(config = BaseMapper.class)
public interface PizzaMapper extends BaseMapper<Pizza, PizzaDTO> {
}
