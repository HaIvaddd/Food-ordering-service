package by.bsuir.foodordering.api.ui;

import by.bsuir.foodordering.api.dto.create.CreateFoodDto;
import by.bsuir.foodordering.api.dto.get.FoodDto;
import by.bsuir.foodordering.core.models.FoodType;
import by.bsuir.foodordering.core.service.FoodService;
import jakarta.validation.Valid;
import java.util.List;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/ui/food")
@AllArgsConstructor
public class FoodControllerUi {

    private final FoodService foodService;

    private void addCommonAttributes(Model model) {
        model.addAttribute("activePage", "food");
        model.addAttribute("allFoodTypes", FoodType.values());
    }

    @GetMapping
    public String showFoodList(Model model) {
        addCommonAttributes(model);
        List<FoodDto> foods = foodService.findAll();
        model.addAttribute("foods", foods);
        return "food-list";
    }

    @GetMapping("/add")
    public String showAddFoodForm(Model model) {
        addCommonAttributes(model);
        model.addAttribute("foodDto", new CreateFoodDto());
        model.addAttribute("pageTitle", "Добавить Еду");
        model.addAttribute("isEditMode", false);
        return "food-form";
    }

    @GetMapping("/edit/{id}")
    public String showEditFoodForm(@PathVariable("id") Long id,
                                   Model model,
                                   RedirectAttributes redirectAttributes) {
        addCommonAttributes(model);
        try {
            FoodDto foodDto = foodService.findById(id);
            model.addAttribute("foodDto", foodDto);
            model.addAttribute("pageTitle", "Редактировать Еду");
            model.addAttribute("isEditMode", true);
            return "food-form";
        } catch (Exception e) {
            redirectAttributes
                    .addFlashAttribute("errorMessage", "Еда с ID " + id + " не найдена.");
            return "redirect:/ui/food";
        }
    }

    @PostMapping("/save")
    public String saveFood(@Valid @ModelAttribute("foodDto") FoodDto foodDto,
                           BindingResult bindingResult,
                           Model model,
                           RedirectAttributes redirectAttributes) {
        try {
            if (foodDto.getId() == null) {
                CreateFoodDto createDto = new CreateFoodDto(
                        foodDto.getName(),
                        foodDto.getPrice(),
                        foodDto.getFoodTypeStr()
                );
                foodService.create(createDto);
                redirectAttributes
                        .addFlashAttribute("successMessage", "Еда успешно добавлена!");
            } else {
                foodService.update(foodDto);
                redirectAttributes
                        .addFlashAttribute("successMessage", "Еда успешно обновлена!");
            }
        } catch (Exception e) {
            redirectAttributes
                    .addFlashAttribute("errorMessage", "Ошибка при сохранении еды: " + e.getMessage());
            return "redirect:/ui/food";
        }

        return "redirect:/ui/food";
    }

    @PostMapping("/delete/{id}")
    public String deleteFood(@PathVariable("id") Long id, RedirectAttributes redirectAttributes) {
        try {
            foodService.delete(id);
            redirectAttributes
                    .addFlashAttribute("successMessage", "Еда успешно удалена!");
        } catch (Exception e) {
            redirectAttributes
                    .addFlashAttribute("errorMessage",
                            "Ошибка при удалении еды: " + e.getMessage());
        }
        return "redirect:/ui/food";
    }
}