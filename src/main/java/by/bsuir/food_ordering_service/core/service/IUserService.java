package by.bsuir.food_ordering_service.core.service;

import by.bsuir.food_ordering_service.api.dto.UserDTO;
import by.bsuir.food_ordering_service.core.objects.User;

import java.util.List;

public interface IUserService {

    UserDTO findById(Long id);
    List<UserDTO> findByUsername(String username);
    List<UserDTO> findAll();
    //void create(UserDTO user);
}
