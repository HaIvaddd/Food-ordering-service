package by.bsuir.foodordering.api;

import by.bsuir.foodordering.core.models.OrderHistoryItem;
import by.bsuir.foodordering.core.service.OrderHistoryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import lombok.AllArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/history")
@AllArgsConstructor
@Tag(name = "Order History API", description = "Endpoint for retrieving placed order history")
public class OrderHistoryController {

    private final OrderHistoryService orderHistoryService;

    @Operation(summary = "Find all order history items",
            description = "Retrieves a list of all past orders placed by users.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200",
                description = "Successfully retrieved all order history items",
                content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                    array = @ArraySchema(schema = @Schema(implementation = OrderHistoryItem.class))
                ))
    })
    @GetMapping
    public List<OrderHistoryItem> findAll() {
        return orderHistoryService.findAll();
    }
}