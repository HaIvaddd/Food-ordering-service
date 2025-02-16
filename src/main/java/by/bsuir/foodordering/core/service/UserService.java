package by.bsuir.foodordering.core.service;

import by.bsuir.foodordering.api.dto.UserDto;
import by.bsuir.foodordering.core.mapper.UserMapper;
import by.bsuir.foodordering.core.repository.UserRepository;
import java.util.List;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;


@Service
@AllArgsConstructor
public class UserService implements BaseUserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;

    @Override
    public UserDto findById(Long id) {
        return userMapper.toDto(userRepository.findById(id));
    }

    @Override
    public List<UserDto> findByUsername(String username) {
        return userMapper.toDtos(userRepository.findByName(username));
    }

    @Override
    public List<UserDto> findAll() {
        return userMapper.toDtos(userRepository.findAll());
    }
}
