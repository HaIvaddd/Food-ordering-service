package by.bsuir.foodordering.api.dto;

import by.bsuir.foodordering.core.objects.BaseUser;
import java.util.List;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class UserDto extends BaseUser {
    private List<Long> ordersId;
}
