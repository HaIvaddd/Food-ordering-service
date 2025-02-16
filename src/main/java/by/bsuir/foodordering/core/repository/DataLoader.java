package by.bsuir.foodordering.core.repository;

import by.bsuir.foodordering.core.objects.Food;
import by.bsuir.foodordering.core.objects.FoodType;
import by.bsuir.foodordering.core.objects.Order;
import by.bsuir.foodordering.core.objects.OrderItem;
import by.bsuir.foodordering.core.objects.User;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import lombok.AllArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class DataLoader implements CommandLineRunner {

    private final UserRepository userRepository;
    private final OrderRepository orderRepository;
    private final FoodRepository foodRepository;

    @Override
    public void run(String... args) {
        OrderItem orderItem1 = new OrderItem(1L, null, null, 1, new BigDecimal(10));
        OrderItem orderItem2 = new OrderItem(2L, null, null, 1, new BigDecimal(10));
        OrderItem orderItem3 = new OrderItem(3L, null, null, 1, new BigDecimal(10));
        OrderItem orderItem4 = new OrderItem(4L, null, null, 1, new BigDecimal(10));
        Order order = new Order(
                1L,
                null,
                LocalDateTime.now(),
                List.of(orderItem1, orderItem2, orderItem3, orderItem4));

        User user1 = new User(
                1L,
                "Maksim",
                "halvaddd@gmail.com",
                List.of(order));
        userRepository.save(user1);

        User user2 = new User(
                2L,
                "Tanya",
                "pupu@gmail.com",
                List.of(order));
        userRepository.save(user2);

        Food food1 = new Food(
                1L,
                "Pizza margherita",
                new BigDecimal("6.99"),
                FoodType.PIZZA);
        orderItem1.setFood(food1);
        foodRepository.save(food1);

        Food food2 = new Food(
                2L,
                "Pizza pepperoni",
                new BigDecimal("8.99"),
                FoodType.PIZZA);
        orderItem2.setFood(food2);
        foodRepository.save(food2);

        Food food3 = new Food(
                3L,
                "Big Mac",
                new BigDecimal("8.99"),
                FoodType.BURGER);
        orderItem3.setFood(food3);
        foodRepository.save(food3);

        Food food4 = new Food(
                4L,
                "Caesar salad",
                new BigDecimal("4.49"),
                FoodType.SALAD);
        orderItem4.setFood(food4);
        foodRepository.save(food4);

        order.setUser(user1);
        orderItem1.setOrder(order);
        orderItem2.setOrder(order);
        orderItem3.setOrder(order);
        orderItem4.setOrder(order);

        orderRepository.save(order);
    }
}
