package by.bsuir.food_ordering_service.core.mapper;

import by.bsuir.food_ordering_service.api.dto.AdminDTO;
import by.bsuir.food_ordering_service.core.objects.Admin;
import org.mapstruct.Mapper;

@Mapper(config = BaseMapper.class)
public interface AdminMapper extends BaseMapper<Admin, AdminDTO> {
}
