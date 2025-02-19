package by.bsuir.foodordering.core.objects;

import java.util.ArrayList;
import java.util.List;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class User {

    private Long id;
    private String name;
    private String email;

    private final List<Order> orders = new ArrayList<>();

    public User(Long id, String name, String email, List<Order> orders) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.orders.addAll(orders);
    }

}
