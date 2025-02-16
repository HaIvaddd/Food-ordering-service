package by.bsuir.foodordering.core.service;

import by.bsuir.foodordering.api.dto.FoodDto;
import by.bsuir.foodordering.core.mapper.FoodMapper;
import by.bsuir.foodordering.core.repository.FoodRepository;
import java.util.List;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class FoodService implements BaseFoodService {

    private final FoodMapper foodMapper;
    private final FoodRepository foodRepository;

    @Override
    public List<FoodDto> findAll() {
        return foodMapper.toDtos(foodRepository.findAll());
    }

    @Override
    public List<FoodDto> findByType(String type) {
        return foodMapper.toDtos(foodRepository.findByType(type));
    }
}
