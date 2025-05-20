package uk.jinhy.server.service.user.domain;

import org.mapstruct.Mapper;
import uk.jinhy.server.api.user.domain.User;

@Mapper(componentModel = "spring")
public interface UserMapper {
    UserEntity toEntity(User user);
    uk.jinhy.server.api.user.domain.User toDomain(UserEntity userEntity);
}
