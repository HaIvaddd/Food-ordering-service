package by.bsuir.foodordering.core.service.impl;

import by.bsuir.foodordering.api.dto.create.CreateUserDto;
import by.bsuir.foodordering.api.dto.get.UserDto;
import by.bsuir.foodordering.core.exception.EntityNotFoundException;
import by.bsuir.foodordering.core.mapper.create.CreateUserMapper;
import by.bsuir.foodordering.core.mapper.get.UserMapper;
import by.bsuir.foodordering.core.models.User;
import by.bsuir.foodordering.core.repository.UserRepository;
import by.bsuir.foodordering.core.service.UserService;
import jakarta.transaction.Transactional;
import java.util.List;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class UserServiceImpl implements UserService {

    private static final String USER_EX = "User not found with id: ";

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final CreateUserMapper createUserMapper;

    @Override
    public UserDto findById(Long id) {
        return userMapper
                .toDto(userRepository
                        .findById(id)
                        .orElseThrow(
                                () -> new EntityNotFoundException(USER_EX + id
                                )
                        )
                );
    }

    @Override
    public UserDto create(CreateUserDto createUserDto) {
        return userMapper.toDto(userRepository.save(createUserMapper.toEntity(createUserDto)));
    }

    @Override
    @Transactional
    public UserDto update(UserDto userDto) {
        User user = userRepository
                .findById(userDto.getId())
                .orElseThrow(() -> new EntityNotFoundException(
                        USER_EX + userDto.getId()));
        return userMapper.toDto(userMapper.merge(user, userDto));
    }

    @Override
    public void deleteById(Long id) {
        if (userRepository.existsById(id)) {
            userRepository.deleteById(id);
        } else {
            throw new EntityNotFoundException(USER_EX + id);
        }
    }

    @Override
    public List<UserDto> findByUsername(String username) {
        return userMapper.toDtos(userRepository.findByName(username));
    }

    @Override
    public List<UserDto> findAll() {
        return userRepository.findAll().stream().map(userMapper::toDto).toList();
    }
}
