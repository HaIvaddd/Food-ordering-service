package by.bsuir.food_ordering_service.core.mapper;

import org.mapstruct.*;

import java.util.List;

@MapperConfig(
        componentModel = "spring",
        builder = @Builder(),
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
        nullValueIterableMappingStrategy = NullValueMappingStrategy.RETURN_DEFAULT
)
public interface BaseMapper<E, D> {
    D toDTO(E entity);
    E toEntity(D dto);
    List<D> toDTOs(List<E> entities);
    E merge(@MappingTarget E entity, D dto);
}
