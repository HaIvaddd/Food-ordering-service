package by.bsuir.foodordering.api.dto.create;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateUserDto {
    private String name;
    private String email;
    private String userTypeStr;
}
