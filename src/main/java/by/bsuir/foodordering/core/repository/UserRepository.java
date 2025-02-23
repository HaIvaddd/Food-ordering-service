package by.bsuir.foodordering.core.repository;

import by.bsuir.foodordering.core.objects.User;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import  java.util.Optional;
import lombok.Getter;
import org.springframework.stereotype.Repository;

@Repository
@Getter
public class UserRepository {

    private final Map<Long, User> users = new HashMap<>();

    public List<User> findAll() {
        return new ArrayList<>(users.values());
    }

    public Optional<User> findById(Long id) {
        return Optional.of(users.get(id));
    }

    public List<User> findByName(String name) {
        return users.values()
                .stream()
                .filter(user -> user.getName().equals(name))
                .toList();
    }

    public User create(User user) {
        return users.put(user.getId(), user);
    }

    public User save(User user) {
        return users.put(user.getId(), user);
    }

    public boolean existsById(Long id) {
        return users.containsKey(id);
    }

    public void deleteById(Long id) {
        users.remove(id);
    }
}
