package by.bsuir.food_ordering_service.core.repository;

import by.bsuir.food_ordering_service.core.objects.*;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Component
public class DataLoader implements CommandLineRunner {

    private final UserRepository userRepository;
    private final OrderRepository orderRepository;

    public DataLoader(UserRepository userRepository, OrderRepository orderRepository) {
        this.userRepository = userRepository;
        this.orderRepository = orderRepository;
    }

    @Override
    public void run(String... args) throws Exception {
        User user;
        OrderItem orderItem1 = new OrderItem(1L, null, null, 1, new BigDecimal(10));
        OrderItem orderItem2 = new OrderItem(2L, null, null, 1, new BigDecimal(10));
        OrderItem orderItem3 = new OrderItem(3L, null, null, 1, new BigDecimal(10));
        OrderItem orderItem4 = new OrderItem(4L, null, null, 1, new BigDecimal(10));
        Food food1;
        Food food2;
        Food food3;
        Food food4;
        Order order = new Order(1L, null, LocalDateTime.now(), List.of(orderItem1, orderItem2, orderItem3, orderItem4));;

        user = new User(1L, "Maksim", "halvaddd@gmail.com", List.of(order));
        food1 = new Food(1L, "Pizza margherita", new BigDecimal("6.99"), FoodType.PIZZA);
        food2 = new Food(2L, "Pizza pepperoni", new BigDecimal("8.99"), FoodType.PIZZA);
        food3 = new Food(3L, "Big Mac", new BigDecimal("8.99"), FoodType.BURGER);
        food4 = new Food(4L, "Caesar salad", new BigDecimal("4.49"), FoodType.SALAD);
        order.setUser(user);
        orderItem1.setFood(food1);
        orderItem1.setOrder(order);
        orderItem2.setFood(food2);
        orderItem2.setOrder(order);
        orderItem3.setFood(food3);
        orderItem3.setOrder(order);
        orderItem4.setFood(food4);
        orderItem4.setOrder(order);

        userRepository.save(user);
        orderRepository.save(order);
        System.out.printf(user.toString());
    }
}
