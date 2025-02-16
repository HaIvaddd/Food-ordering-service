package by.bsuir.foodordering.core.mapper;

import by.bsuir.foodordering.api.dto.UserDto;
import by.bsuir.foodordering.core.objects.Order;
import by.bsuir.foodordering.core.objects.User;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-02-16T22:45:04+0300",
    comments = "version: 1.6.3, compiler: IncrementalProcessingEnvironment from gradle-language-java-8.12.1.jar, environment: Java 23.0.2 (Oracle Corporation)"
)
@Component
public class UserMapperImpl implements UserMapper {

    @Override
    public User toEntity(UserDto dto) {
        if ( dto == null ) {
            return null;
        }

        Long id = null;
        String name = null;
        String email = null;

        id = dto.getId();
        name = dto.getName();
        email = dto.getEmail();

        List<Order> orders = null;

        User user = new User( id, name, email, orders );

        return user;
    }

    @Override
    public List<UserDto> toDtos(List<User> entities) {
        if ( entities == null ) {
            return new ArrayList<UserDto>();
        }

        List<UserDto> list = new ArrayList<UserDto>( entities.size() );
        for ( User user : entities ) {
            list.add( toDto( user ) );
        }

        return list;
    }

    @Override
    public User merge(User entity, UserDto dto) {
        if ( dto == null ) {
            return entity;
        }

        if ( dto.getId() != null ) {
            entity.setId( dto.getId() );
        }
        if ( dto.getName() != null ) {
            entity.setName( dto.getName() );
        }
        if ( dto.getEmail() != null ) {
            entity.setEmail( dto.getEmail() );
        }

        return entity;
    }

    @Override
    public UserDto toDto(User entity) {
        if ( entity == null ) {
            return null;
        }

        UserDto userDto = new UserDto();

        userDto.setOrdersId( mapOrderToIdOrder( entity.getOrders() ) );
        userDto.setId( entity.getId() );
        userDto.setName( entity.getName() );
        userDto.setEmail( entity.getEmail() );

        return userDto;
    }
}
