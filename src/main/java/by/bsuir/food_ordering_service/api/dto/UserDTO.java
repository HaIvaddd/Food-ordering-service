package by.bsuir.food_ordering_service.api.dto;
import by.bsuir.food_ordering_service.core.objects.AAccount;
import by.bsuir.food_ordering_service.core.objects.Order;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
public class UserDTO extends AAccount {
    private List<Long> ordersId;
}
