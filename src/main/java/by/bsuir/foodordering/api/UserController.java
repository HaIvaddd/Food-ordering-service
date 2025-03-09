package by.bsuir.foodordering.api;

import by.bsuir.foodordering.api.dto.create.CreateUserDto;
import by.bsuir.foodordering.api.dto.get.UserDto;
import by.bsuir.foodordering.core.service.impl.UserServiceImpl;
import java.util.List;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/users")
@AllArgsConstructor
public class UserController {

    private final UserServiceImpl userServiceImpl;

    @GetMapping
    public List<UserDto> findAll() {
        return userServiceImpl.findAll();
    }

    @GetMapping({"/{id}"})
    public UserDto findById(@PathVariable Long id) {
        return userServiceImpl.findById(id);
    }

    @PostMapping("/create")
    public UserDto createUser(@RequestBody CreateUserDto createUserDto) {
        return userServiceImpl.create(createUserDto);
    }

    @DeleteMapping("/del/{id}")
    public void deleteUser(@PathVariable("id") Long id) {
        userServiceImpl.deleteById(id);
    }

    @PutMapping("/update")
    public UserDto updateUser(@RequestBody UserDto userDto) {
        return userServiceImpl.update(userDto);
    }
}
