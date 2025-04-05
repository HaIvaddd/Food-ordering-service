package by.bsuir.foodordering.service;

import by.bsuir.foodordering.api.dto.create.CreateUserDto;
import by.bsuir.foodordering.api.dto.get.UserDto;
import by.bsuir.foodordering.core.exception.EntityNotFoundException;
import by.bsuir.foodordering.core.mapper.create.CreateUserMapper;
import by.bsuir.foodordering.core.mapper.get.UserMapper;
import by.bsuir.foodordering.core.models.User;
import by.bsuir.foodordering.core.models.UserType;
import by.bsuir.foodordering.core.repository.UserRepository;
import by.bsuir.foodordering.core.service.impl.UserServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {
    @Mock
    private UserRepository userRepository;
    @Mock
    private UserMapper userMapper;
    @Mock
    private CreateUserMapper createUserMapper;

    @InjectMocks
    private UserServiceImpl userService;

    private User testUser;
    private UserDto testUserDto;
    private CreateUserDto testCreateUserDto;
    private final Long userId = 1L;
    private final String username = "testuser";

    @BeforeEach
    void setUp() {
        testUser = new User(userId, username, "test@test.com", UserType.USER);
        testUserDto = new UserDto();
        testUserDto.setId(userId);
        testUserDto.setName(username);
        testUserDto.setEmail("test@test.com");
        testUserDto.setUserTypeStr("USER");

        testCreateUserDto = new CreateUserDto();
        testCreateUserDto.setName(username);
        testCreateUserDto.setEmail("test@test.com");
        testCreateUserDto.setUserTypeStr("USER");
    }

    @Test
    void findById_whenUserExists_shouldReturnUserDto() {

        when(userRepository.findById(userId)).thenReturn(Optional.of(testUser));
        when(userMapper.toDto(testUser)).thenReturn(testUserDto);

        UserDto result = userService.findById(userId);

        assertNotNull(result);
        assertEquals(userId, result.getId());
        assertEquals(username, result.getName());

        verify(userRepository, times(1)).findById(userId);
        verify(userMapper, times(1)).toDto(testUser);
    }

    @Test
    void findById_whenUserNotFound_shouldThrowEntityNotFoundException() {
        when(userRepository.findById(userId)).thenReturn(Optional.empty());
        EntityNotFoundException exception = assertThrows(
                EntityNotFoundException.class,
                () -> userService.findById(userId)
        );

        assertTrue(exception.getMessage().contains(String.valueOf(userId)));

        verify(userRepository, times(1)).findById(userId);
        verify(userMapper, never()).toDto(any(User.class));
    }

    @Test
    void create_shouldSaveUserAndReturnDto() {

        User userToSave = new User(null, username, "test@test.com", UserType.USER);
        when(createUserMapper.toEntity(testCreateUserDto)).thenReturn(userToSave);

        when(userRepository.save(userToSave)).thenReturn(testUser);

        when(userMapper.toDto(testUser)).thenReturn(testUserDto);

        UserDto result = userService.create(testCreateUserDto);

        assertNotNull(result);
        assertEquals(testUserDto.getId(), result.getId());
        assertEquals(testUserDto.getName(), result.getName());

        verify(createUserMapper, times(1)).toEntity(testCreateUserDto);
        verify(userRepository, times(1)).save(userToSave);
        verify(userMapper, times(1)).toDto(testUser);
    }

    @Test
    void update_whenUserExists_shouldFetchMergeAndMapToDto() {
        when(userRepository.findById(userId)).thenReturn(Optional.of(testUser));

        User mergedUser = new User(userId, "Updated Name", "updated@test.com", UserType.USER);
        when(userMapper.merge(testUser, testUserDto)).thenReturn(mergedUser);

        UserDto updatedUserDto = new UserDto();
        updatedUserDto.setId(userId);
        updatedUserDto.setName("Updated Name");
        updatedUserDto.setEmail("updated@test.com");
        updatedUserDto.setUserTypeStr("USER");
        when(userMapper.toDto(mergedUser)).thenReturn(updatedUserDto);

        UserDto result = userService.update(testUserDto);

        assertNotNull(result);
        assertEquals(updatedUserDto.getId(), result.getId());
        assertEquals(updatedUserDto.getName(), result.getName());
        assertEquals(updatedUserDto.getEmail(), result.getEmail());
        assertEquals(updatedUserDto.getUserTypeStr(), result.getUserTypeStr());

        verify(userRepository, times(1)).findById(userId);
        verify(userMapper, times(1)).merge(testUser, testUserDto);
        verify(userMapper, times(1)).toDto(mergedUser);
    }

    @Test
    void update_whenUserNotFound_shouldThrowEntityNotFoundException() {
        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        EntityNotFoundException exception = assertThrows(
                EntityNotFoundException.class,
                () -> userService.update(testUserDto)
        );

        assertTrue(exception.getMessage().contains(String.valueOf(userId)));

        verify(userRepository, times(1)).findById(userId);
        verify(userMapper, never()).merge(any(User.class), any(UserDto.class));
        verify(userMapper, never()).toDto(any(User.class));
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    void deleteById_whenUserExists_shouldCallRepositoryDelete() {

        when(userRepository.existsById(userId)).thenReturn(true);

        userService.deleteById(userId);

        verify(userRepository, times(1)).existsById(userId);
        verify(userRepository, times(1)).deleteById(userId);
    }

    @Test
    void deleteById_whenUserNotFound_shouldThrowEntityNotFoundException() {
        when(userRepository.existsById(userId)).thenReturn(false);

        EntityNotFoundException exception = assertThrows(
                EntityNotFoundException.class,
                () -> userService.deleteById(userId)
        );

        assertTrue(exception.getMessage().contains(String.valueOf(userId)));

        // Verify
        verify(userRepository, times(1)).existsById(userId);
        verify(userRepository, never()).deleteById(anyLong()); // Убеждаемся, что удаление НЕ вызывалось
    }

    @Test
    void findByUsername_whenUsersFound_shouldReturnDtoList() {
        List<User> userList = List.of(testUser);
        List<UserDto> userDtoList = List.of(testUserDto);

        when(userRepository.findByName(username)).thenReturn(userList);
        when(userMapper.toDtos(userList)).thenReturn(userDtoList);


        List<UserDto> result = userService.findByUsername(username);

        assertNotNull(result);
        assertFalse(result.isEmpty());
        assertEquals(1, result.size());
        assertEquals(testUserDto.getId(), result.getFirst().getId());

        verify(userRepository, times(1)).findByName(username);
        verify(userMapper, times(1)).toDtos(userList);
    }

    @Test
    void findByUsername_whenNoUsersFound_shouldReturnEmptyList() {
        List<User> emptyUserList = Collections.emptyList();
        List<UserDto> emptyUserDtoList = Collections.emptyList();

        when(userRepository.findByName(username)).thenReturn(emptyUserList);
        when(userMapper.toDtos(emptyUserList)).thenReturn(emptyUserDtoList); // Или можно вернуть Collections.emptyList()

        List<UserDto> result = userService.findByUsername(username);

        assertNotNull(result);
        assertTrue(result.isEmpty());

        verify(userRepository, times(1)).findByName(username);
        verify(userMapper, times(1)).toDtos(emptyUserList);
    }

    @Test
    void findAll_whenUsersExist_shouldReturnDtoList() {
        User anotherUser = new User(2L, "another", "another@test.com", UserType.USER);
        UserDto anotherUserDto = new UserDto();
        anotherUserDto.setId(2L);
        anotherUserDto.setName("another");
        anotherUserDto.setEmail("another@test.com");
        anotherUserDto.setUserTypeStr("USER");

        List<User> userList = List.of(testUser, anotherUser);

        when(userRepository.findAll()).thenReturn(userList);
        when(userMapper.toDto(testUser)).thenReturn(testUserDto);
        when(userMapper.toDto(anotherUser)).thenReturn(anotherUserDto);

        List<UserDto> result = userService.findAll();

        assertNotNull(result);
        assertEquals(2, result.size());
        assertTrue(result.contains(testUserDto));
        assertTrue(result.contains(anotherUserDto));

        verify(userRepository, times(1)).findAll();
        verify(userMapper, times(1)).toDto(testUser);
        verify(userMapper, times(1)).toDto(anotherUser);
    }

    @Test
    void findAll_whenNoUsersExist_shouldReturnEmptyList() {
        when(userRepository.findAll()).thenReturn(Collections.emptyList());

        List<UserDto> result = userService.findAll();

        assertNotNull(result);
        assertTrue(result.isEmpty());

        verify(userRepository, times(1)).findAll();
        verify(userMapper, never()).toDto(any(User.class));
    }
}