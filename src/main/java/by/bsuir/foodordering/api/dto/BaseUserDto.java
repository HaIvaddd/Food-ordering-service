package by.bsuir.foodordering.api.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public abstract class BaseUserDto {
    private Long id;
    private String name;
    private String email;
}
