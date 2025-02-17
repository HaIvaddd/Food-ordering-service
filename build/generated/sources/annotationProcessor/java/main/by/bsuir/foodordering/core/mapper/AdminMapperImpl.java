package by.bsuir.foodordering.core.mapper;

import by.bsuir.foodordering.api.dto.AdminDto;
import by.bsuir.foodordering.core.objects.Admin;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-02-17T09:19:55+0300",
    comments = "version: 1.6.3, compiler: IncrementalProcessingEnvironment from gradle-language-java-8.12.1.jar, environment: Java 23.0.2 (Oracle Corporation)"
)
@Component
public class AdminMapperImpl implements AdminMapper {

    @Override
    public AdminDto toDto(Admin entity) {
        if ( entity == null ) {
            return null;
        }

        AdminDto adminDto = new AdminDto();

        adminDto.setId( entity.getId() );
        adminDto.setName( entity.getName() );
        adminDto.setEmail( entity.getEmail() );
        adminDto.setPassword( entity.getPassword() );

        return adminDto;
    }

    @Override
    public Admin toEntity(AdminDto dto) {
        if ( dto == null ) {
            return null;
        }

        String password = null;

        password = dto.getPassword();

        Admin admin = new Admin( password );

        admin.setId( dto.getId() );
        admin.setName( dto.getName() );
        admin.setEmail( dto.getEmail() );

        return admin;
    }

    @Override
    public List<AdminDto> toDtos(List<Admin> entities) {
        if ( entities == null ) {
            return new ArrayList<AdminDto>();
        }

        List<AdminDto> list = new ArrayList<AdminDto>( entities.size() );
        for ( Admin admin : entities ) {
            list.add( toDto( admin ) );
        }

        return list;
    }

    @Override
    public Admin merge(Admin entity, AdminDto dto) {
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
        if ( dto.getPassword() != null ) {
            entity.setPassword( dto.getPassword() );
        }

        return entity;
    }
}
