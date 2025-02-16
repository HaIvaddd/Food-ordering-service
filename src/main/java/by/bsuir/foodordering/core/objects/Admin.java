package by.bsuir.foodordering.core.objects;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
public class Admin extends BaseUser {
    private String password;
}
