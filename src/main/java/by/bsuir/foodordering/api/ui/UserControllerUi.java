package by.bsuir.foodordering.api.ui;

import by.bsuir.foodordering.api.dto.create.CreateUserDto;
import by.bsuir.foodordering.api.dto.get.UserDto;
import by.bsuir.foodordering.core.service.UserService; // Интерфейс
import by.bsuir.foodordering.core.models.UserType; // Твой enum UserType
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/ui/users") // UI путь для пользователей
@AllArgsConstructor
public class UserControllerUi {

    private static final Logger log = LoggerFactory.getLogger(UserControllerUi.class);
    private final UserService userService; // Инжектируем сервис

    private void addCommonAttributes(Model model, String activePage) {
        model.addAttribute("activePage", activePage);
        model.addAttribute("allUserTypes", UserType.values());
    }

    @GetMapping
    public String showUserList(Model model) {
        addCommonAttributes(model, "users");
        model.addAttribute("pageTitle", "Список Пользователей");
        try {
            List<UserDto> users = userService.findAll();
            model.addAttribute("users", users);
        } catch (Exception e) {
            log.error("Error fetching user list: {}", e.getMessage(), e);
            model.addAttribute("errorMessage", "Не удалось загрузить список пользователей.");
            model.addAttribute("users", List.of());
        }
        return "user-list"; // Шаблон: templates/user-list.html
    }

    @GetMapping("/add")
    public String showAddUserForm(Model model) {
        addCommonAttributes(model, "users");
        model.addAttribute("pageTitle", "Добавить Пользователя");
        model.addAttribute("userDto", new UserDto());
        model.addAttribute("isEditMode", false);
        model.addAttribute("formAction", "/ui/users/save"); // URL для сохранения
        return "user-form"; // Шаблон: templates/user-form.html
    }

    @GetMapping("/edit/{id}")
    public String showEditUserForm(@PathVariable("id") Long id, Model model, RedirectAttributes redirectAttributes) {
        addCommonAttributes(model, "users");
        model.addAttribute("pageTitle", "Редактировать Пользователя #" + id);
        try {
            UserDto userDto = userService.findById(id);
            model.addAttribute("userDto", userDto);
            model.addAttribute("isEditMode", true);
            model.addAttribute("formAction", "/ui/users/save"); // Тот же URL для сохранения
            return "user-form";
        } catch (Exception e) {
            log.error("Error fetching user for edit with id {}: {}", id, e.getMessage(), e);
            redirectAttributes.addFlashAttribute("errorMessage", "Пользователь с ID " + id + " не найден.");
            return "redirect:/ui/users";
        }
    }

    @PostMapping("/save")
    public String saveUser(@Valid @ModelAttribute("userDto") UserDto userDto, // Принимаем UserDto
                           BindingResult bindingResult,
                           Model model, // Нужна для возврата на форму при ошибке
                           RedirectAttributes redirectAttributes) {

        if (userDto.getName() == null || userDto.getName().isBlank()
                || userDto.getEmail() == null || userDto.getEmail().isBlank()
                || userDto.getUserTypeStr() == null || userDto.getUserTypeStr().isBlank()) {
            bindingResult.reject("globalError", "Не все обязательные поля заполнены."); // Пример глобальной ошибки
        }

        if (bindingResult.hasErrors()) {
            log.warn("Validation errors saving user: {}", bindingResult.getAllErrors());
            addCommonAttributes(model, "users"); // Передаем типы снова
            model.addAttribute("pageTitle", userDto.getId() == null ? "Добавить Пользователя" : "Редактировать Пользователя #" + userDto.getId());
            model.addAttribute("isEditMode", userDto.getId() != null);
            model.addAttribute("formAction", "/ui/users/save");
            // userDto уже есть в модели благодаря @ModelAttribute
            return "user-form"; // Возвращаем на форму с ошибками
        }

        try {
            if (userDto.getId() == null) { // --- Создание ---
                log.info("Attempting to CREATE user with email: {}", userDto.getEmail());
                // Конвертируем UserDto в CreateUserDto
                CreateUserDto createDto = new CreateUserDto();
                createDto.setName(userDto.getName());
                createDto.setEmail(userDto.getEmail());
                createDto.setUserTypeStr(userDto.getUserTypeStr());
                userService.create(createDto);
                redirectAttributes.addFlashAttribute("successMessage", "Пользователь успешно создан!");
            } else { // --- Обновление ---
                log.info("Attempting to UPDATE user with ID: {}", userDto.getId());
                // Метод update принимает UserDto целиком
                userService.update(userDto);
                redirectAttributes.addFlashAttribute("successMessage", "Пользователь успешно обновлен!");
            }
            return "redirect:/ui/users"; // Успешно - редирект на список
        } catch (Exception e) {
            log.error("Error saving user (ID: {}): {}", userDto.getId(), e.getMessage(), e);
            // Добавляем сообщение об ошибке
            redirectAttributes.addFlashAttribute("errorMessage", "Ошибка при сохранении пользователя: " + e.getMessage());
            // Редирект обратно на форму редактирования или добавления
            if (userDto.getId() == null) {
                return "redirect:/ui/users/add"; // Редирект на форму добавления
            } else {
                return "redirect:/ui/users/edit/" + userDto.getId(); // Редирект на форму редактирования
            }
        }
    }

    // --- Обработка УДАЛЕНИЯ ---
    @PostMapping("/delete/{id}")
    public String deleteUser(@PathVariable("id") Long id, RedirectAttributes redirectAttributes) {
        log.info("Attempting to DELETE user with ID: {}", id);
        try {
            userService.deleteById(id);
            redirectAttributes.addFlashAttribute("successMessage", "Пользователь успешно удален!");
        } catch (Exception e) {
            // Обработка ошибок (например, если пользователь связан с заказами и удаление запрещено)
            log.error("Error deleting user with ID {}: {}", id, e.getMessage(), e);
            redirectAttributes.addFlashAttribute("errorMessage", "Ошибка при удалении пользователя: " + e.getMessage());
        }
        return "redirect:/ui/users"; // Возвращаемся к списку
    }
}