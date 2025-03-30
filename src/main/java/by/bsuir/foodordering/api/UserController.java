package by.bsuir.foodordering.api;

import by.bsuir.foodordering.api.dto.create.CreateUserDto;
import by.bsuir.foodordering.api.dto.get.UserDto;
import by.bsuir.foodordering.core.service.impl.UserServiceImpl;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.util.List;
import java.util.Map;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/users")
@AllArgsConstructor
@Tag(name = "User Management API", description = "Endpoints for managing application users")
public class UserController {

    private final UserServiceImpl userServiceImpl;

    @Operation(summary = "Find all users",
            description = "Retrieves a list of all registered users.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successfully retrieved all users",
                content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                        array = @ArraySchema(schema = @Schema(implementation = UserDto.class))))
    })
    @GetMapping
    public List<UserDto> findAll() {
        return userServiceImpl.findAll();
    }

    @Operation(summary = "Find a user by ID",
            description = "Retrieves details of a specific user by their unique ID.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successfully retrieved the user",
                content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                        schema = @Schema(implementation = UserDto.class))),
        @ApiResponse(responseCode = "404", description = "User with the specified ID not found",
                content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                        schema = @Schema(implementation = Map.class)))
    })
    @GetMapping({"/{id}"})
    public UserDto findById(
            @Parameter(description = "The unique ID of the user", required = true, example = "1",
                    schema = @Schema(type = "integer", format = "int64"))
            @PathVariable Long id) {
        return userServiceImpl.findById(id);
    }

    @Operation(summary = "Create a new user", description = "Registers a new user in the system.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "User created successfully",
                content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                        schema = @Schema(implementation = UserDto.class))),
        @ApiResponse(responseCode = "400", description = "Invalid input data",
                content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                        schema = @Schema(implementation = Map.class)))
    })
    @PostMapping("/create")
    public ResponseEntity<UserDto> createUser(
            @RequestBody(description = "Data for the new user", required = true,
                    content = @Content(schema = @Schema(implementation = CreateUserDto.class)))
            @Valid @org.springframework
                    .web.bind.annotation.RequestBody CreateUserDto createUserDto) {
        UserDto createdUser = userServiceImpl.create(createUserDto);
        return new ResponseEntity<>(createdUser, HttpStatus.CREATED);
    }

    @Operation(summary = "Delete a user by ID",
            description = "Removes a user from the system by their ID.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "User deleted successfully"),
        @ApiResponse(responseCode = "404", description = "User with the specified ID not found",
                content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                        schema = @Schema(implementation = Map.class)))
    })
    @DeleteMapping("/del/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteUser(
            @Parameter(description = "The unique ID of the user to delete",
                    required = true, example = "10",
                    schema = @Schema(type = "integer", format = "int64"))
            @PathVariable("id") Long id) {
        userServiceImpl.deleteById(id);
    }

    @Operation(summary = "Update an existing user",
            description = "Modifies an existing user's details.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "User updated successfully",
                content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                        schema = @Schema(implementation = UserDto.class))),
        @ApiResponse(responseCode = "404", description = "User with the specified ID not found",
                content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                        schema = @Schema(implementation = Map.class))),
        @ApiResponse(responseCode = "400", description = "Invalid input data (if validation added)",
                content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                        schema = @Schema(implementation = Map.class)))
    })
    @PutMapping("/update")
    public UserDto updateUser(
            @RequestBody(description = "Updated data for the user (must include the ID)",
                    required = true,
                    content = @Content(schema = @Schema(implementation = UserDto.class)))
            @Valid @org.springframework.web.bind.annotation.RequestBody UserDto userDto) {
        return userServiceImpl.update(userDto);
    }
}