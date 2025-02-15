package by.bsuir.food_ordering_service.core.repository;

import by.bsuir.food_ordering_service.core.objects.Admin;
import lombok.Getter;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Repository
@Getter
public class AdminRepository {

    private final Map<Long, Admin> admins = new HashMap<>();

    public List<Admin> findAll() {
        return new ArrayList<>(admins.values());
    }

    public Admin findById(Long id) {
        return admins.get(id);
    }

    public List<Admin> findByName(String name) {
        return admins.values().stream().filter(admin -> admin.getName().equals(name)).collect(Collectors.toList());
    }

    public Admin save(Admin admin) {
        return admins.put(admin.getId(), admin);
    }
}
