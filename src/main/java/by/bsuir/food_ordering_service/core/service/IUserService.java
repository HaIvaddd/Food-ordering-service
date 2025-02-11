package by.bsuir.food_ordering_service.core.service;

import by.bsuir.food_ordering_service.api.dto.UserDTO;
import by.bsuir.food_ordering_service.core.objects.User;

import java.util.List;

public interface IUserService {

    List<UserDTO> findById(Long id);
    UserDTO findByUsername(String username);
    void create(UserDTO user);
}
