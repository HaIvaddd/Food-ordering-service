package by.bsuir.foodordering.api.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AdminDto extends UserDto {
    private String password;
}
