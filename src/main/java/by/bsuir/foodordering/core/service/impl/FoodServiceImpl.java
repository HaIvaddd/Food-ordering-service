package by.bsuir.foodordering.core.service.impl;

import by.bsuir.foodordering.api.dto.create.CreateFoodDto;
import by.bsuir.foodordering.api.dto.get.FoodDto;
import by.bsuir.foodordering.core.cache.Cache;
import by.bsuir.foodordering.core.exception.EntityNotFoundException;
import by.bsuir.foodordering.core.mapper.create.CreateFoodMapper;
import by.bsuir.foodordering.core.mapper.get.FoodMapper;
import by.bsuir.foodordering.core.models.Food;
import by.bsuir.foodordering.core.models.FoodType;
import by.bsuir.foodordering.core.repository.FoodRepository;
import by.bsuir.foodordering.core.service.FoodService;
import jakarta.transaction.Transactional;
import java.util.List;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class FoodServiceImpl implements FoodService {

    private final FoodMapper foodMapper;
    private final CreateFoodMapper createFoodMapper;
    private final FoodRepository foodRepository;
    private final Cache foodCache;

    @Override
    public FoodDto findById(Long id) {
        if (foodCache.get(id) != null) {
            return foodMapper.toDto(foodCache.get(id));
        } else {
            return foodMapper.toDto(
                    foodRepository
                            .findById(id)
                            .orElseThrow(() -> new EntityNotFoundException(id.toString())
                            )
            );
        }
    }

    @Override
    public FoodDto create(CreateFoodDto foodDto) {
        if (foodDto == null) {
            throw new IllegalArgumentException();
        }
        Food food = createFoodMapper.toEntity(foodDto);
        return foodMapper.toDto(foodRepository.save(foodCache.put(food)));
    }

    @Override
    @Transactional
    public FoodDto update(FoodDto foodDto) {
        if (foodDto == null) {
            throw new IllegalArgumentException();
        }

        Food existingFood = foodCache.get(foodDto.getId());

        if  (existingFood == null) {
            existingFood = foodRepository.findById(foodDto.getId())
                    .orElseThrow(
                            () -> new EntityNotFoundException(
                                    "Food not found with id: " + foodDto.getId()
                            )
                    );
        }

        Food mergedFood = foodMapper.merge(existingFood, foodDto);

        foodCache.put(mergedFood);

        return foodMapper.toDto(mergedFood);
    }

    @Override
    public void delete(Long foodId) {
        if (!foodRepository.existsById(foodId)) {
            throw new EntityNotFoundException(foodId.toString());
        }
        foodCache.deleteFood(foodId);
        foodRepository.deleteById(foodId);
    }

    @Override
    public List<FoodDto> findAll() {
        return foodRepository.findAll().stream().map(foodMapper::toDto).toList();
    }

    @Override
    public List<FoodDto> findByType(String type) {
        FoodType foodType = FoodType.valueOf(type.toUpperCase());
        return foodRepository.findByFoodType(foodType).stream().map(foodMapper::toDto).toList();
    }
}
