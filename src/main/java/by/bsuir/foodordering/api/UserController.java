package by.bsuir.foodordering.api;

import by.bsuir.foodordering.api.dto.UserDto;
import by.bsuir.foodordering.core.service.UserService;
import java.util.List;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/user")
@AllArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping
    public List<UserDto> findAll() {
        return userService.findAll();
    }

    @GetMapping({"/{id}"})
    public UserDto findById(@PathVariable Long id) {
        return userService.findById(id);
    }
}
