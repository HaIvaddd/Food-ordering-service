package by.bsuir.food_ordering_service.core.objects;

import lombok.*;

import java.math.BigDecimal;

@Setter
@Getter
@AllArgsConstructor
public class Food {

    private Long id;

    private String name;
    private BigDecimal price;

    private FoodType foodType;
    //добавить String description
    //List<OrderItem> orderItems = new ArrayList<>();
}
