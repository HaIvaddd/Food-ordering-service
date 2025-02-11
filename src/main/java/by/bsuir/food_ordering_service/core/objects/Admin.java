package by.bsuir.food_ordering_service.core.objects;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
public class Admin extends AAccount{
    private String password;
}
