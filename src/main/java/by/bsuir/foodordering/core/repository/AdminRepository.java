package by.bsuir.foodordering.core.repository;

import by.bsuir.foodordering.core.objects.Admin;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.Getter;
import org.springframework.stereotype.Repository;



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
        return admins.values()
                .stream()
                .filter(admin -> admin.getName().equals(name))
                .toList();
    }

    public Admin save(Admin admin) {
        return admins.put(admin.getId(), admin);
    }
}
