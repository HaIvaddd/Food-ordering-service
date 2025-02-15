package by.bsuir.food_ordering_service.core.objects;

import lombok.*;

import java.util.ArrayList;
import java.util.List;


@Getter
public class User extends AAccount{
    private List<Order> orders = new ArrayList<>();

    public User(Long id, String name, String email, List<Order> orders) {
        super(id, name, email);
        this.orders = orders;
    }

    @Override
    public String toString() {
        return this.getName() + " " + this.getEmail();
    }
}
