package by.bsuir.foodordering.core.service;

import by.bsuir.foodordering.api.dto.create.CreateFoodDto;
import by.bsuir.foodordering.api.dto.get.FoodDto;
import by.bsuir.foodordering.core.exception.EntityNotFoundException;
import by.bsuir.foodordering.core.mapper.create.CreateFoodMapper;
import by.bsuir.foodordering.core.mapper.get.FoodMapper;
import by.bsuir.foodordering.core.objects.Food;
import by.bsuir.foodordering.core.repository.FoodRepository;
import java.util.List;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class FoodService implements BaseFoodService {

    private final FoodMapper foodMapper;
    private final CreateFoodMapper createFoodMapper;
    private final FoodRepository foodRepository;

    @Override
    public FoodDto findById(Long id) {
        return foodMapper.toDto(
                foodRepository
                        .findById(id).orElseThrow(() -> new EntityNotFoundException(id.toString())
                        )
        );
    }

    @Override
    public FoodDto create(CreateFoodDto foodDto) {
        if (foodDto == null) {
            throw new IllegalArgumentException();
        }
        return foodMapper.toDto(foodRepository.saveFood(createFoodMapper.toEntity(foodDto)));
    }

    @Override
    public FoodDto update(FoodDto foodDto) {
        if (foodDto == null) {
            throw new IllegalArgumentException();
        }
        Food existingFood = foodRepository.findById(foodDto.getId())
                .orElseThrow(
                        () -> new EntityNotFoundException(
                                "Food not found with id: " + foodDto.getId()
                        )
                );
        Food mergedFood = foodMapper.merge(existingFood, foodDto);
        return foodMapper.toDto(mergedFood);
    }

    @Override
    public void delete(Long foodId) {
        if (!foodRepository.existsById(foodId)) {
            throw new EntityNotFoundException(foodId.toString());
        }
        foodRepository.deleteById(foodId);
    }

    @Override
    public List<FoodDto> findAll() {
        return foodMapper.toDtos(foodRepository.findAll());
    }

    @Override
    public List<FoodDto> findByType(String type) {
        return foodMapper.toDtos(foodRepository.findByType(type));
    }

}
