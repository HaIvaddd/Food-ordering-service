package by.bsuir.foodordering.core.service;

import by.bsuir.foodordering.api.dto.UserDto;
import java.util.List;

public interface BaseUserService {

    UserDto findById(Long id);

    List<UserDto> findByUsername(String username);

    List<UserDto> findAll();
}
