package uk.jinhy.server.service.user.oauth2;

import uk.jinhy.server.service.user.domain.UserEntity;

import java.util.Map;

public interface OAuth2UserResponse {

    Map<String, Object> getAttributes();

    String getProvider();

    String getProviderId();

    UserEntity toUserEntity();

    default String getOAuth2UserId() {
        return getProvider() + " " + getProviderId();
    }

}
