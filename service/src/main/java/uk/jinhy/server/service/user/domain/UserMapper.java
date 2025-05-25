package uk.jinhy.server.service.user.domain;

import org.mapstruct.Mapper;
import uk.jinhy.server.api.user.domain.User;
import uk.jinhy.server.api.user.presentation.dto.response.AuthenticatedUserInfoResponseDto;

@Mapper(componentModel = "spring")
public interface UserMapper {
    User toDomain(UserEntity userEntity);
    AuthenticatedUserInfoResponseDto toAuthenticatedUserInfoResponseDto(User user);
}
