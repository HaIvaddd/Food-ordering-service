package by.bsuir.foodordering.api;

import by.bsuir.foodordering.api.dto.create.CreateOrderDto;
import by.bsuir.foodordering.api.dto.get.OrderDto;
import by.bsuir.foodordering.api.dto.get.OrderInfoDto;
import by.bsuir.foodordering.core.annotation.Timed;
import by.bsuir.foodordering.core.service.impl.OrderServiceImpl;
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
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/orders")
@AllArgsConstructor
@Tag(name = "Order Management API", description = "Endpoints for managing customer orders")
public class OrderController {

    private final OrderServiceImpl orderServiceImpl;

    @Operation(summary = "Find all orders", description = "Retrieves a summary list of all orders.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successfully retrieved all orders",
                content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                    array = @ArraySchema(schema = @Schema(implementation = OrderInfoDto.class))))
    })
    @GetMapping
    public List<OrderInfoDto> findAll() {
        return orderServiceImpl.findAll();
    }

    @Operation(summary = "Find an order by ID",
            description = "Retrieves detailed information for a specific order by its ID.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successfully retrieved the order",
                content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                        schema = @Schema(implementation = OrderDto.class))),
        @ApiResponse(responseCode = "404", description = "Order with the specified ID not found",
                content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                        schema = @Schema(implementation = Map.class)))
    })
    @GetMapping("/{id}")
    public OrderDto findById(
            @Parameter(description = "The unique ID of the order", required = true, example = "1",
                    schema = @Schema(type = "integer", format = "int64"))
            @PathVariable Long id) {
        return orderServiceImpl.findById(id);
    }

    @Operation(summary = "Mark an order as placed",
            description = "Marks an existing order as 'ordered' by its ID and adds it to history.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Order successfully marked as placed",
                content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                        schema = @Schema(implementation = OrderDto.class))),
        @ApiResponse(responseCode = "404", description = "Order with the specified ID not found",
                content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                        schema = @Schema(implementation = Map.class)))
    })
    @PatchMapping("/make/{id}")
    public OrderDto makeOrder(
            @Parameter(description = "The unique ID of the order to mark as placed",
                    required = true, example = "2",
                    schema = @Schema(type = "integer", format = "int64"))
            @PathVariable("id") Long id) {
        return orderServiceImpl.makeOrderById(id);
    }

    @Timed
    @Operation(summary = "Create a new order",
            description = "Creates a new order with specified items and user ID.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Order created successfully",
                content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                        schema = @Schema(implementation = OrderDto.class))),
        @ApiResponse(responseCode = "400",
                description = "Invalid input data",
                content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                        schema = @Schema(implementation = Map.class)))
    })
    @PostMapping("/create")
    public ResponseEntity<OrderDto> createOrder(
            @RequestBody(description = "Data for the new order, including user ID and order items",
                    required = true,
                    content = @Content(schema = @Schema(implementation = CreateOrderDto.class)))
            @Valid @org.springframework
                    .web.bind.annotation.RequestBody CreateOrderDto createOrderDto) {
        OrderDto createdOrder = orderServiceImpl.create(createOrderDto);
        return new ResponseEntity<>(createdOrder, HttpStatus.CREATED);
    }

    @Operation(summary = "Delete an order by ID",
            description = "Removes an order from the system by its ID.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "Order deleted successfully"),
        @ApiResponse(responseCode = "404", description = "Order with the specified ID not found",
                content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                        schema = @Schema(implementation = Map.class)))
    })
    @DeleteMapping("/del/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteOrder(
            @Parameter(description = "The unique ID of the order to delete",
                    required = true, example = "3",
                    schema = @Schema(type = "integer", format = "int64"))
            @PathVariable("id") Long id) {
        orderServiceImpl.deleteById(id);
    }

    @Operation(summary = "Update an existing order",
            description = "Modifies an existing order. "
                    + "Can be used to change user or order items before the order is placed.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200",
                description = "Order updated successfully",
                content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                        schema = @Schema(implementation = OrderDto.class))),
        @ApiResponse(responseCode = "400",
                description = "Invalid input data",
                content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                        schema = @Schema(implementation = Map.class))),
        @ApiResponse(responseCode = "404",
                description = "Order or User with the specified ID not found",
                content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                        schema = @Schema(implementation = Map.class)))
    })
    @PutMapping("/update")
    public OrderDto updateOrder(
            @RequestBody(description = "Updated data for the order (must include the order ID)",
                    required = true,
                    content = @Content(schema = @Schema(implementation = OrderDto.class)))
            @org.springframework.web.bind.annotation.RequestBody OrderDto orderDto) {
        return orderServiceImpl.update(orderDto);
    }

    @Operation(summary = "Find orders by exact food item count",
            description = "Retrieves orders containing exactly the "
                    + "specified number of distinct food items.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successfully retrieved orders",
                content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                    array = @ArraySchema(schema = @Schema(implementation = OrderInfoDto.class))))
    })
    @GetMapping("/by-food-count") // Измененный путь
    public List<OrderInfoDto> findOrdersByCountFood(
            @Parameter(description = "The exact number of distinct food items in the order",
                    required = true, example = "3",
                    schema = @Schema(type = "integer", format = "int32"))
            @RequestParam("count") int count) { // Измененный параметр запроса
        return orderServiceImpl.findByCountFood(count);
    }

    @Operation(summary = "Find orders containing a specific food item name",
            description = "Retrieves orders that include at least "
                    + "one food item with the specified name.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successfully retrieved orders",
                content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                    array = @ArraySchema(schema = @Schema(implementation = OrderInfoDto.class))))
    })
    @GetMapping("/by-food-name") // Измененный путь
    public List<OrderInfoDto> findByFoodName(
            @Parameter(description = "The name of the food item to search for within orders",
                    required = true, example = "Pepperoni")
            @RequestParam("foodName") String foodName) {
        return orderServiceImpl.findByFoodName(foodName);
    }
}
