package by.bsuir.food_ordering_service.core.objects;

import lombok.*;

import java.util.ArrayList;
import java.util.List;


@Getter
@AllArgsConstructor
@RequiredArgsConstructor
public class User extends AAccount{
    private List<Order> orders = new ArrayList<>();
}
