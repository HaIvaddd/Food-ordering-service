package by.bsuir.foodordering.api.ui;

import by.bsuir.foodordering.api.dto.create.CreateOrderDto;
import by.bsuir.foodordering.api.dto.create.UpdateOrderDto;
import by.bsuir.foodordering.api.dto.get.FoodDto;
import by.bsuir.foodordering.api.dto.get.OrderInfoDto;
import by.bsuir.foodordering.api.dto.get.OrderItemDto;
import by.bsuir.foodordering.api.dto.get.UserDto;
import by.bsuir.foodordering.core.service.FoodService;
import by.bsuir.foodordering.core.service.OrderService; // Используй интерфейс
import by.bsuir.foodordering.core.service.UserService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/ui/orders") // Префикс для UI путей заказов
@AllArgsConstructor
public class OrderControllerUi {

    // Оставим логгер на случай ошибок, но уберем детальные логи успеха
    private final OrderService orderService;
    private final UserService userService; // Добавили UserService
    private final FoodService foodService; // Добавили FoodService
    private final ObjectMapper objectMapper;
    // --- Общий метод для добавления атрибутов модели ---
    private void addCommonAttributes(Model model, String activePage) {
        model.addAttribute("activePage", activePage);
    }

    // --- Отображение списка заказов ---
    @GetMapping
    public String showOrderList(Model model) {
        addCommonAttributes(model, "orders"); // Активная страница - "Заказы"
        try {
            List<OrderInfoDto> orders = orderService.findAll();
            model.addAttribute("orders", orders);
        } catch (Exception e) {
            model.addAttribute("errorMessage", "Не удалось загрузить список заказов.");
            // Можно добавить пустой список, чтобы страница не падала
            model.addAttribute("orders", List.of());
        }
        return "order-list"; // Имя HTML: templates/order-list.html
    }

    // --- Отображение деталей заказа ---
    @GetMapping("/details/{id}")
    public String showOrderDetails(@PathVariable("id") Long id, Model model, RedirectAttributes redirectAttributes) {
        addCommonAttributes(model, "orders"); // Остаемся в разделе "Заказы"
        try {
            OrderInfoDto orderInfo = orderService.findById(id);
            model.addAttribute("orderInfo", orderInfo);
            model.addAttribute("pageTitle", "Детали Заказа #" + id); // Динамический заголовок
            return "order-details"; // Имя HTML: templates/order-details.html
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Заказ с ID " + id + " не найден или произошла ошибка.");
            return "redirect:/ui/orders";
        }
    }

    // --- Обработка оформления заказа ---
    @PostMapping("/make/{id}")
    public String makeOrder(@PathVariable("id") Long id, RedirectAttributes redirectAttributes) {
        try {
            orderService.makeOrderById(id);
            redirectAttributes.addFlashAttribute("successMessage", "Заказ #" + id + " успешно оформлен.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Ошибка при оформлении заказа #" + id + ": " + e.getMessage());
        }
        return "redirect:/ui/orders";
    }

    // --- Обработка удаления заказа ---
    @PostMapping("/delete/{id}")
    public String deleteOrder(@PathVariable("id") Long id, RedirectAttributes redirectAttributes) {
        try {
            orderService.deleteById(id);
            redirectAttributes.addFlashAttribute("successMessage", "Заказ #" + id + " успешно удален.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Ошибка при удалении заказа #" + id + ": " + e.getMessage());
        }
        return "redirect:/ui/orders";
    }

    @GetMapping("/add")
    public String showCreateOrderForm(Model model) {
        addCommonAttributes(model, "orders"); // Остаемся в разделе заказов
        try {
            List<UserDto> users = userService.findAll();
            List<FoodDto> foods = foodService.findAll();

            model.addAttribute("allUsers", users);
            model.addAttribute("allFoods", foods);
            // Передаем пустой объект CreateOrderDto для связи с формой (хотя заполняться он будет через JS)
            model.addAttribute("createOrderDto", new CreateOrderDto());
            model.addAttribute("pageTitle", "Создать Заказ");

        } catch (Exception e) {
            model.addAttribute("errorMessage", "Не удалось загрузить данные для создания заказа.");
            model.addAttribute("allUsers", List.of()); // Пустые списки, чтобы страница не упала
            model.addAttribute("allFoods", List.of());
        }
        model.addAttribute("isEditMode", false); // Явно указываем режим
        model.addAttribute("formAction", "/ui/orders/create"); // URL для создания
        return "order-create";
    }

    @PostMapping("/create")
    public String createOrder(@Valid @ModelAttribute CreateOrderDto createOrderDto,
                              BindingResult bindingResult, // Для отлова ошибок валидации DTO
                              RedirectAttributes redirectAttributes,
                              Model model) { // Модель нужна, если вернемся на форму при ошибке

        // Проверяем ошибки валидации (например, не выбран пользователь или пустой список)
        // Также может понадобиться кастомная валидация, что список createOrderItems не пустой
        if (bindingResult.hasErrors() || createOrderDto.getCreateOrderItems() == null || createOrderDto.getCreateOrderItems().isEmpty()) {
            redirectAttributes.addFlashAttribute("errorMessage", "Ошибка валидации: Не выбран пользователь или список позиций пуст.");
            return "redirect:/ui/orders/add";
        }

        try {
            orderService.create(createOrderDto);
            redirectAttributes.addFlashAttribute("successMessage", "Заказ успешно создан!");
            return "redirect:/ui/orders"; // Перенаправляем на список заказов
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Ошибка при создании заказа: " + e.getMessage());
            return "redirect:/ui/orders/add"; // Возвращаемся на форму создания при ошибке
        }
    }

    @GetMapping("/edit/{id}")
    public String showEditOrderForm(@PathVariable("id") Long id, Model model, RedirectAttributes redirectAttributes) {
        addCommonAttributes(model, "orders");
        try {
            OrderInfoDto orderInfo = orderService.findById(id);

            // Проверка, можно ли редактировать (например, если уже оформлен)
            if (orderInfo.isOrdered()) {
                redirectAttributes.addFlashAttribute("errorMessage", "Заказ #" + id + " уже оформлен и не может быть отредактирован.");
                return "redirect:/ui/orders";
            }

            List<UserDto> users = userService.findAll();
            List<FoodDto> foods = foodService.findAll();

            model.addAttribute("pageTitle", "Редактировать Заказ #" + id);
            model.addAttribute("allUsers", users);
            model.addAttribute("allFoods", foods);
            model.addAttribute("isEditMode", true); // Указываем режим редактирования
            model.addAttribute("formAction", "/ui/orders/update"); // URL для обновления

            // Подготовка данных для предзаполнения формы
            model.addAttribute("orderId", orderInfo.getId()); // Передаем ID заказа
            model.addAttribute("selectedUserId", orderInfo.getUserDto().getId()); // Передаем ID выбранного пользователя

            Map<Long, Map<String, Object>> initialItemsMap = new HashMap<>();
            Map<String, Long> foodNameToIdMap = foods.stream()
                    .collect(Collectors.toMap(FoodDto::getName, FoodDto::getId, (id1, id2) -> id1)); // На случай дубликатов имен

            if (orderInfo.getOrderItemDtos() != null) {
                for (OrderItemDto itemDto : orderInfo.getOrderItemDtos()) {
                    Long foodId = foodNameToIdMap.get(itemDto.getFoodName());
                    if (foodId != null) {
                        Map<String, Object> itemData = new HashMap<>();
                        itemData.put("name", itemDto.getFoodName());
                        itemData.put("count", itemDto.getCount());
                        initialItemsMap.put(foodId, itemData);
                    }
                }
            }
            // Передаем начальные данные как JSON строку в модель
            try {
                model.addAttribute("initialOrderItemsJson", objectMapper.writeValueAsString(initialItemsMap));
            } catch (JsonProcessingException e) {
                model.addAttribute("initialOrderItemsJson", "{}"); // Пустой объект в случае ошибки
                model.addAttribute("errorMessage", "Ошибка при загрузке позиций заказа для редактирования.");
            }


            // Возвращаем тот же шаблон, что и для создания
            return "order-create";

        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Заказ с ID " + id + " не найден или произошла ошибка.");
            return "redirect:/ui/orders";
        }
    }

    @PostMapping("/update")
    public String updateOrder(@Valid @ModelAttribute UpdateOrderDto updateOrderDto, // Используем новый DTO
                              BindingResult bindingResult,
                              RedirectAttributes redirectAttributes,
                              Model model) {

        if (bindingResult.hasErrors() || updateOrderDto.getCreateOrderItems() == null || updateOrderDto.getCreateOrderItems().isEmpty()) {
            redirectAttributes.addFlashAttribute("errorMessage", "Ошибка валидации при обновлении заказа.");
            return "redirect:/ui/orders/edit/" + updateOrderDto.getOrderId();
        }

        try {
            orderService.updateOrder(updateOrderDto); // Название метода может быть другим
            redirectAttributes.addFlashAttribute("successMessage", "Заказ #" + updateOrderDto.getOrderId() + " успешно обновлен!");
            return "redirect:/ui/orders"; // Возвращаемся к списку
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Ошибка при обновлении заказа: " + e.getMessage());
            return "redirect:/ui/orders/edit/" + updateOrderDto.getOrderId();
        }
    }
}