package by.bsuir.foodordering.service;

import by.bsuir.foodordering.api.dto.create.CreateFoodDto;
import by.bsuir.foodordering.api.dto.get.FoodDto;
import by.bsuir.foodordering.core.cache.Cache;
import by.bsuir.foodordering.core.exception.EntityNotFoundException;
import by.bsuir.foodordering.core.exception.FoodTypeException;
import by.bsuir.foodordering.core.mapper.create.CreateFoodMapper;
import by.bsuir.foodordering.core.mapper.get.FoodMapper;
import by.bsuir.foodordering.core.models.Food;
import by.bsuir.foodordering.core.models.FoodType;
import by.bsuir.foodordering.core.repository.FoodRepository;
import by.bsuir.foodordering.core.service.impl.FoodServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class FoodServiceTest {

    @Mock private FoodMapper foodMapper;
    @Mock private CreateFoodMapper createFoodMapper;
    @Mock private FoodRepository foodRepository;
    @Mock private Cache foodCache;

    @InjectMocks
    private FoodServiceImpl foodService;

    @Captor
    private ArgumentCaptor<Food> foodCaptor;

    private final Long foodId = 1L;
    private final String foodName = "Pizza Margherita";
    private Food testFood;
    private FoodDto testFoodDto;
    private CreateFoodDto testCreateFoodDto;

    @BeforeEach
    void setUp() {
        testFood = new Food(foodId, foodName, BigDecimal.valueOf(15.99), FoodType.PIZZA);

        testFoodDto = new FoodDto();
        testFoodDto.setId(foodId);
        testFoodDto.setName(foodName);
        testFoodDto.setPrice(BigDecimal.valueOf(15.99));
        testFoodDto.setFoodTypeStr("PIZZA");

        testCreateFoodDto = new CreateFoodDto();
        testCreateFoodDto.setName(foodName);
        testCreateFoodDto.setPrice(BigDecimal.valueOf(15.99));
        testCreateFoodDto.setFoodTypeStr("PIZZA");
    }

    @Test
    void findById_whenCacheHit_shouldReturnDtoFromCache() {
        when(foodCache.get(foodId)).thenReturn(testFood);
        when(foodMapper.toDto(testFood)).thenReturn(testFoodDto);

        FoodDto result = foodService.findById(foodId);

        assertNotNull(result);
        assertEquals(testFoodDto, result);

        verify(foodCache).get(foodId);
        verify(foodMapper).toDto(testFood);
        verify(foodRepository, never()).findById(anyLong());
        verify(foodCache, never()).put(any(Food.class));
    }

    @Test
    void findById_whenCacheMissAndFoundInRepo_shouldFetchPutToCacheAndReturnDto() {
        when(foodCache.get(foodId)).thenReturn(null);
        when(foodRepository.findById(foodId)).thenReturn(Optional.of(testFood));
        when(foodCache.put(any(Food.class))).thenAnswer(invocation -> invocation.getArgument(0));
        when(foodMapper.toDto(testFood)).thenReturn(testFoodDto);

        FoodDto result = foodService.findById(foodId);

        assertNotNull(result);
        assertEquals(testFoodDto, result);

        verify(foodCache).get(foodId);
        verify(foodRepository).findById(foodId);
        verify(foodCache).put(foodCaptor.capture());
        assertEquals(testFood, foodCaptor.getValue());
        verify(foodMapper).toDto(testFood);
    }

    @Test
    void findById_whenCacheMissAndNotFoundInRepo_shouldThrowEntityNotFoundException() {
        when(foodCache.get(foodId)).thenReturn(null);
        when(foodRepository.findById(foodId)).thenReturn(Optional.empty());

        EntityNotFoundException exception = assertThrows(
                EntityNotFoundException.class,
                () -> foodService.findById(foodId)
        );
        assertTrue(exception.getMessage().contains(String.valueOf(foodId)));

        verify(foodCache).get(foodId);
        verify(foodRepository).findById(foodId);
        verify(foodCache, never()).put(any(Food.class));
        verify(foodMapper, never()).toDto(any(Food.class));
    }

    @Test
    void findByName_whenCacheHit_shouldReturnDtoFromCache() {
        when(foodCache.get(foodName)).thenReturn(testFood);
        when(foodMapper.toDto(testFood)).thenReturn(testFoodDto);

        FoodDto result = foodService.findByName(foodName);

        assertEquals(testFoodDto, result);

        verify(foodCache).get(foodName);
        verify(foodMapper).toDto(testFood);
        verify(foodRepository, never()).findByName(anyString());
    }

    @Test
    void findByName_whenCacheMissAndFoundInRepo_shouldFetchAndReturnDto() {
        when(foodCache.get(foodName)).thenReturn(null);
        when(foodRepository.findByName(foodName)).thenReturn(testFood);
        when(foodMapper.toDto(testFood)).thenReturn(testFoodDto);

        FoodDto result = foodService.findByName(foodName);

        assertEquals(testFoodDto, result);

        verify(foodCache).get(foodName);
        verify(foodRepository).findByName(foodName);
        verify(foodMapper).toDto(testFood);
        verify(foodCache, never()).put(any(Food.class));
    }

    @Test
    void findByName_whenCacheMissAndNotFoundInRepo_shouldReturnDtoForNull() {
        when(foodCache.get(foodName)).thenReturn(null);
        when(foodRepository.findByName(foodName)).thenReturn(null);
        when(foodMapper.toDto(null)).thenReturn(null);

        FoodDto result = foodService.findByName(foodName);

        assertNull(result);

        verify(foodCache).get(foodName);
        verify(foodRepository).findByName(foodName);
        verify(foodMapper).toDto(null);
        verify(foodCache, never()).put(any(Food.class));
    }

    @Test
    void create_whenValidDto_shouldMapSaveAndReturnDto() throws FoodTypeException {

        Food foodToSave = new Food(null, foodName, testCreateFoodDto.getPrice(), FoodType.valueOf(testCreateFoodDto.getFoodTypeStr()));
        Food savedFood = testFood;

        when(createFoodMapper.toEntity(testCreateFoodDto)).thenReturn(foodToSave);
        when(foodRepository.save(foodToSave)).thenReturn(savedFood);
        when(foodMapper.toDto(savedFood)).thenReturn(testFoodDto);

        FoodDto result = foodService.create(testCreateFoodDto);

        assertEquals(testFoodDto, result);

        verify(createFoodMapper).toEntity(testCreateFoodDto);
        verify(foodRepository).save(foodToSave);
        verify(foodMapper).toDto(savedFood);
        verifyNoInteractions(foodCache);
    }

    @Test
    void create_whenDtoIsNull_shouldThrowIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class, () -> foodService.create(null));
        verifyNoInteractions(createFoodMapper, foodRepository, foodMapper, foodCache);
    }

    @Test
    void update_whenCacheHit_shouldMergeUpdateCacheAndReturnDto() {
        FoodDto dtoToUpdate = new FoodDto();
        dtoToUpdate.setId(foodId);
        dtoToUpdate.setName("Updated Pizza");
        dtoToUpdate.setPrice(BigDecimal.valueOf(18.00));
        dtoToUpdate.setFoodTypeStr("PIZZA");

        Food existingFood = testFood;
        Food mergedFood = new Food(foodId, "Updated Pizza", BigDecimal.valueOf(18.00), FoodType.PIZZA);
        FoodDto resultDto = new FoodDto();
        resultDto.setId(foodId);
        resultDto.setName("Updated Pizza");
        resultDto.setPrice(BigDecimal.valueOf(18.00));
        resultDto.setFoodTypeStr("PIZZA");

        when(foodRepository.findById(foodId)).thenReturn(Optional.of(existingFood));
        when(foodMapper.merge(existingFood, dtoToUpdate)).thenReturn(mergedFood);
        when(foodCache.get(foodId)).thenReturn(existingFood);
        when(foodCache.put(mergedFood)).thenReturn(mergedFood);
        when(foodMapper.toDto(mergedFood)).thenReturn(resultDto);

        FoodDto result = foodService.update(dtoToUpdate);

        assertEquals(resultDto, result);

        verify(foodRepository).findById(foodId);
        verify(foodMapper).merge(existingFood, dtoToUpdate);
        verify(foodCache).get(foodId);
        verify(foodCache, times(1)).put(foodCaptor.capture());
        assertEquals(mergedFood, foodCaptor.getValue());
        verify(foodMapper).toDto(mergedFood);
    }

    @Test
    void update_whenCacheMiss_shouldMergeUpdateCacheAndReturnDto() {
        FoodDto dtoToUpdate = new FoodDto();
        dtoToUpdate.setId(foodId);
        dtoToUpdate.setName("Updated Pizza");
        dtoToUpdate.setPrice(BigDecimal.valueOf(18.00));
        dtoToUpdate.setFoodTypeStr("PIZZA");

        Food existingFood = testFood;
        Food mergedFood = new Food(foodId, "Updated Pizza", BigDecimal.valueOf(18.00), FoodType.PIZZA);
        FoodDto resultDto = new FoodDto();
        resultDto.setId(foodId);
        resultDto.setName("Updated Pizza");
        resultDto.setPrice(BigDecimal.valueOf(18.00));
        resultDto.setFoodTypeStr("PIZZA");

        when(foodRepository.findById(foodId)).thenReturn(Optional.of(existingFood));
        when(foodMapper.merge(existingFood, dtoToUpdate)).thenReturn(mergedFood);
        when(foodCache.get(foodId)).thenReturn(null);
        when(foodCache.put(mergedFood)).thenReturn(mergedFood);
        when(foodMapper.toDto(mergedFood)).thenReturn(resultDto);

        FoodDto result = foodService.update(dtoToUpdate);

        assertEquals(resultDto, result);

        verify(foodRepository).findById(foodId);
        verify(foodMapper).merge(existingFood, dtoToUpdate);
        verify(foodCache).get(foodId);
        verify(foodCache, times(2)).put(foodCaptor.capture());
        assertEquals(mergedFood, foodCaptor.getValue());
        verify(foodMapper).toDto(mergedFood);
    }

    @Test
    void update_whenDtoIsNull_shouldThrowIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class, () -> foodService.update(null));
        verifyNoInteractions(foodRepository, foodMapper, foodCache);
    }

    @Test
    void update_whenFoodNotFound_shouldThrowEntityNotFoundException() {
        when(foodRepository.findById(foodId)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> foodService.update(testFoodDto));

        verify(foodRepository).findById(foodId);
        verifyNoMoreInteractions(foodRepository);
        verifyNoInteractions(foodMapper, foodCache);
    }

    @Test
    void delete_whenFoodExists_shouldDeleteFromCacheAndRepo() {
        when(foodRepository.existsById(foodId)).thenReturn(true);
        doNothing().when(foodCache).deleteFood(foodId);
        doNothing().when(foodRepository).deleteById(foodId);

        foodService.delete(foodId);

        verify(foodRepository).existsById(foodId);
        verify(foodCache).deleteFood(foodId);
        verify(foodRepository).deleteById(foodId);
    }

    @Test
    void delete_whenFoodNotFound_shouldThrowEntityNotFoundException() {
        when(foodRepository.existsById(foodId)).thenReturn(false);

        assertThrows(EntityNotFoundException.class, () -> foodService.delete(foodId));

        verify(foodRepository).existsById(foodId);
        verify(foodCache, never()).deleteFood(anyLong());
        verify(foodRepository, never()).deleteById(anyLong());
    }

    @Test
    void findAll_whenFoodExists_shouldMapAndReturnDtoList() {
        Food food2 = new Food(2L, "Burger", BigDecimal.valueOf(12.50), FoodType.BURGER);
        FoodDto foodDto2 = new FoodDto();
        foodDto2.setId(2L);
        foodDto2.setName("Burger");
        foodDto2.setPrice(BigDecimal.valueOf(12.50));
        foodDto2.setFoodTypeStr("BURGER");

        List<Food> foodList = List.of(testFood, food2);
        List<FoodDto> expectedDtoList = List.of(testFoodDto, foodDto2);

        when(foodRepository.findAll()).thenReturn(foodList);
        when(foodMapper.toDto(testFood)).thenReturn(testFoodDto);
        when(foodMapper.toDto(food2)).thenReturn(foodDto2);

        List<FoodDto> result = foodService.findAll();

        assertEquals(expectedDtoList, result);

        verify(foodRepository).findAll();
        verify(foodMapper).toDto(testFood);
        verify(foodMapper).toDto(food2);
        verifyNoInteractions(foodCache);
    }

    @Test
    void findAll_whenNoFood_shouldReturnEmptyList() {
        when(foodRepository.findAll()).thenReturn(Collections.emptyList());

        List<FoodDto> result = foodService.findAll();

        assertTrue(result.isEmpty());

        verify(foodRepository).findAll();
        verify(foodMapper, never()).toDto(any(Food.class));
        verifyNoInteractions(foodCache);
    }

    @Test
    void findByType_whenValidTypeAndFoodExists_shouldMapAndReturnDtoList() {
        FoodType type = FoodType.PIZZA;
        List<Food> foodList = List.of(testFood);
        List<FoodDto> expectedDtoList = List.of(testFoodDto);

        when(foodRepository.findByFoodType(type)).thenReturn(foodList);
        when(foodMapper.toDto(testFood)).thenReturn(testFoodDto);

        List<FoodDto> result = foodService.findByType("PIZZA");

        assertEquals(expectedDtoList, result);

        verify(foodRepository).findByFoodType(type);
        verify(foodMapper).toDto(testFood);
        verifyNoInteractions(foodCache);
    }

    @Test
    void findByType_whenValidTypeAndNoFood_shouldReturnEmptyList() {
        FoodType type = FoodType.SUSHI;
        when(foodRepository.findByFoodType(type)).thenReturn(Collections.emptyList());

        List<FoodDto> result = foodService.findByType("SUSHI");

        assertTrue(result.isEmpty());

        verify(foodRepository).findByFoodType(type);
        verify(foodMapper, never()).toDto(any(Food.class));
        verifyNoInteractions(foodCache);
    }

    @Test
    void findByType_whenInvalidTypeString_shouldThrowFoodTypeException() {
        String invalidType = "INVALID_FOOD_TYPE";

        FoodTypeException exception = assertThrows(
                FoodTypeException.class,
                () -> foodService.findByType(invalidType)
        );
        assertTrue(exception.getMessage().contains(invalidType));

        verify(foodRepository, never()).findByFoodType(any(FoodType.class)); // Репозиторий не вызывался
        verify(foodMapper, never()).toDto(any(Food.class));
        verifyNoInteractions(foodCache);
    }
}