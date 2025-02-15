package by.bsuir.food_ordering_service.core.service;

import by.bsuir.food_ordering_service.api.dto.UserDTO;
import by.bsuir.food_ordering_service.core.mapper.UserMapper;
import by.bsuir.food_ordering_service.core.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class UserService implements IUserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;

    @Override
    public UserDTO findById(Long id) {
        return userMapper.toDTO(userRepository.findById(id));
    }

    @Override
    public List<UserDTO> findByUsername(String username) {
        return userMapper.toDTOs(userRepository.findByName(username));
    }

    @Override
    public List<UserDTO> findAll() {
        return userMapper.toDTOs(userRepository.findAll());
    }
}
