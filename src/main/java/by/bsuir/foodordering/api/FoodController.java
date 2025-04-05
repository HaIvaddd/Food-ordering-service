package by.bsuir.foodordering.api;

import by.bsuir.foodordering.api.dto.create.CreateFoodDto;
import by.bsuir.foodordering.api.dto.get.FoodDto;
import by.bsuir.foodordering.core.annotation.Timed;
import by.bsuir.foodordering.core.service.impl.FoodServiceImpl;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/food")
@AllArgsConstructor
@Tag(name = "Food API", description = "Operations related to managing food items")
public class FoodController {

    private final FoodServiceImpl foodServiceImpl;

    @Operation(summary = "Find food items by type",
            description = "Retrieves a list of food items matching the specified food type.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successfully retrieved food items",
                content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                        array = @ArraySchema(schema = @Schema(implementation = FoodDto.class)))),
        @ApiResponse(responseCode = "400", description = "Invalid food type provided",
                content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                        schema = @Schema(implementation = Map.class)))
    })
    @GetMapping("by-type")
    public List<FoodDto> findFoodByType(
            @Parameter(description = "The type of food",
                    required = true, example = "PIZZA")
            @RequestParam("type") String foodType) {
        return foodServiceImpl.findByType(foodType);
    }

    @Timed
    @Operation(summary = "Find a food item by ID",
            description = "Retrieves details of a specific food item by its unique ID.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successfully retrieved the food item",
                content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                        schema = @Schema(implementation = FoodDto.class))),
        @ApiResponse(responseCode = "404",
                description = "Food item with the specified ID not found",
                content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                        schema = @Schema(implementation = Map.class)))
    })
    @GetMapping("/{id}")
    public FoodDto findFoodById(
            @Parameter(description = "The unique ID of the food item",
                    required = true, example = "1",
                    schema = @Schema(type = "integer", format = "int64"))
            @PathVariable Long id) {
        return foodServiceImpl.findById(id);
    }

    @Timed
    @Operation(summary = "Find a food item by name",
            description = "Retrieves details of a specific food item by its unique name.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successfully retrieved the food item",
                content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                        schema = @Schema(implementation = FoodDto.class))),
        @ApiResponse(responseCode = "404",
                description = "Food item not found",
                content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                        schema = @Schema(implementation = Map.class)))
    })
    @GetMapping("/by-name")
    public FoodDto findFoodByName(
            @Parameter(description = "The type of food",
                    required = true, example = "PIZZA")
            @RequestParam("name") String foodName) {
        return foodServiceImpl.findByName(foodName);
    }

    @Operation(summary = "Create a new food item",
            description = "Adds a new food item to the system based on the provided data.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Food item created successfully",
                content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                        schema = @Schema(implementation = FoodDto.class))),
        @ApiResponse(responseCode = "400",
                description = "Invalid input data provided (validation error or null body)",
                content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                        schema = @Schema(implementation = Map.class)))
    })

    @PostMapping("/create")
    public ResponseEntity<FoodDto> createFood(
            @RequestBody(description = "Data for the new food item", required = true,
                    content = @Content(schema = @Schema(implementation = CreateFoodDto.class)))
            @Valid @org.springframework.web
                    .bind.annotation.RequestBody CreateFoodDto createFoodDto) {
        FoodDto createdFood = foodServiceImpl.create(createFoodDto);
        return new ResponseEntity<>(createdFood, HttpStatus.CREATED);
    }

    @Operation(summary = "Delete a food item by ID",
            description = "Removes a food item from the system by its unique ID.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "Food item deleted successfully"),
        @ApiResponse(responseCode = "404",
                description = "Food item with the specified ID not found",
                content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                        schema = @Schema(implementation = Map.class)))
    })
    @DeleteMapping("/del/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteFood(
            @Parameter(description = "The unique ID of the food item to delete",
                    required = true, example = "5",
                    schema = @Schema(type = "integer", format = "int64"))
            @PathVariable("id") Long id) {
        foodServiceImpl.delete(id);
    }

    @Operation(summary = "Update an existing food item",
            description = "Modifies an existing food item. "
                    + "The entire food item data, including the ID, should be provided.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Food item updated successfully",
                content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                        schema = @Schema(implementation = FoodDto.class))),
        @ApiResponse(responseCode = "400",
                description = "Invalid input data provided (request body is null)",
                content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                        schema = @Schema(implementation = Map.class))),
        @ApiResponse(responseCode = "404",
                description = "Food item with the specified ID not found",
                content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                        schema = @Schema(implementation = Map.class)))
    })
    @PutMapping("/update")
    public FoodDto updateFood(
            @RequestBody(description = "Updated data for the food item (must include the ID)",
                    required = true,
                    content = @Content(schema = @Schema(implementation = FoodDto.class)))
            @Valid @org.springframework.web.bind.annotation.RequestBody FoodDto foodDto) {
        return foodServiceImpl.update(foodDto);
    }

    @Operation(summary = "Find all food items",
            description = "Retrieves a list of all food items available in the system.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successfully retrieved all food items",
                content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                        array = @ArraySchema(schema = @Schema(implementation = FoodDto.class))))
    })
    @GetMapping
    public List<FoodDto> findFoodAll() {
        return foodServiceImpl.findAll();
    }


    @PostMapping("/bulk")
    @Operation(
            summary = "Create multiple food items",
            description = "Accepts a list of food items (as CreateFoodDto) in the request "
                    + "body and creates them in bulk. Returns a list of the created "
                    + "food items (as FoodDto) including their generated IDs."
    )
    @ApiResponses(value = {
        @ApiResponse(
                responseCode = "201",
                description = "Food items created successfully",
                content = @Content(
                        mediaType = "application/json",
                        schema = @Schema(implementation = FoodDto.class)
                )
            ),
        @ApiResponse(
                responseCode = "400",
                description = "Invalid input data",
                content = @Content
            ),
        @ApiResponse(
                responseCode = "500",
                description = "Internal server error during creation",
                content = @Content
            )
    })
    public ResponseEntity<List<FoodDto>> createFoodsBulk(
            @Valid @org.springframework.web.bind.annotation.RequestBody
            List<CreateFoodDto> createFoodDtos) {

        List<FoodDto> createdDtos = foodServiceImpl.createBulk(createFoodDtos);

        return ResponseEntity.status(HttpStatus.CREATED).body(createdDtos);
    }
}

