package by.bsuir.food_ordering_service.api.dto;
import by.bsuir.food_ordering_service.core.objects.AAccount;

import java.util.List;

public class UserDTO extends AAccount {
    private List<Long> ordersId;
}
