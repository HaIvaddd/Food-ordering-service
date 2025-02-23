package by.bsuir.foodordering.core.mapper.create;

import by.bsuir.foodordering.api.dto.create.CreateUserDto;
import by.bsuir.foodordering.core.exception.UserTypeException;
import by.bsuir.foodordering.core.mapper.BaseMapper;
import by.bsuir.foodordering.core.objects.User;
import by.bsuir.foodordering.core.objects.UserType;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

@Mapper(config = BaseMapper.class)
public interface CreateUserMapper extends BaseMapper<User, CreateUserDto> {
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "userType",
            source = "userTypeStr",
            qualifiedByName = "toUserTypeFromUserTypeStrMap")
    User toEntity(CreateUserDto createUserDto);

    @Named("toUserTypeFromUserTypeStrMap")
    default UserType toUserTypeFromUserTypeStrMap(String userTypeStr) {
        try {
            return UserType.valueOf(userTypeStr.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new UserTypeException(userTypeStr);
        }
    }
}
