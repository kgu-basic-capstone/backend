package uk.jinhy.server.service.user.domain;

import org.mapstruct.Mapper;
import uk.jinhy.server.api.domain.User;
import uk.jinhy.server.service.domain.UserEntity;

@Mapper(componentModel = "spring")
public interface UserMapper {
    UserEntity toEntity(User user);
    User toDomain(UserEntity userEntity);
}
