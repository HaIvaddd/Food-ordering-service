package by.bsuir.foodordering.core.service;

import by.bsuir.foodordering.api.dto.create.CreateUserDto;
import by.bsuir.foodordering.api.dto.get.UserDto;
import java.util.List;

public interface BaseUserService {

    UserDto findById(Long id);

    UserDto create(CreateUserDto createUserDto);

    UserDto update(UserDto userDto);

    List<UserDto> findByUsername(String username);

    List<UserDto> findAll();

    void deleteById(Long id);
}
