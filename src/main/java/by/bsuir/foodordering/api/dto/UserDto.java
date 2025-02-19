package by.bsuir.foodordering.api.dto;

import java.util.List;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class UserDto {
    private Long id;
    private String name;
    private String email;
    private List<Long> ordersId;
}
