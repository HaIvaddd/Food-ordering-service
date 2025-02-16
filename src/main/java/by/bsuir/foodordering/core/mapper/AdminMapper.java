package by.bsuir.foodordering.core.mapper;

import by.bsuir.foodordering.api.dto.AdminDto;
import by.bsuir.foodordering.core.objects.Admin;
import org.mapstruct.Mapper;

@Mapper(config = BaseMapper.class)
public interface AdminMapper extends BaseMapper<Admin, AdminDto> {
}
