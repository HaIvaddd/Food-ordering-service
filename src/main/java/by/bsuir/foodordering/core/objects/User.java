package by.bsuir.foodordering.core.objects;

import java.util.ArrayList;
import java.util.List;
import lombok.Getter;




@Getter
public class User extends BaseUser {

    private final List<Order> orders = new ArrayList<>();

    public User(Long id, String name, String email, List<Order> orders) {
        super(id, name, email);
        this.orders.addAll(orders);
    }

    @Override
    public String toString() {
        return this.getName() + " " + this.getEmail();
    }
}
