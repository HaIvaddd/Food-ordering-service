package by.bsuir.food_ordering_service.core.repository;

import by.bsuir.food_ordering_service.core.objects.User;
import lombok.Getter;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Repository
@Getter
public class UserRepository {

    private final Map<Long, User> users = new HashMap<>();

    public List<User> findAll() {
        return new ArrayList<>(users.values());
    }

    public User findById(Long id) {
        return users.get(id);
    }

    public List<User> findByName(String name) {
        return users.values().stream().filter(user -> user.getName().equals(name)).collect(Collectors.toList());
    }

    public User save(User user) {
        return users.put(user.getId(), user);
    }
}
