package uk.jinhy.server.service.user.oauth2;

import org.springframework.security.oauth2.core.user.OAuth2User;
import uk.jinhy.server.service.user.domain.UserEntity;

import java.util.Map;

public class GoogleOAuth2UserResponse implements OAuth2UserResponse {

    private final Map<String, Object> attributes;

    public GoogleOAuth2UserResponse(OAuth2User oAuth2User) {
        attributes = oAuth2User.getAttributes();
    }

    @Override
    public Map<String, Object> getAttributes() {
        return Map.copyOf(attributes);
    }

    @Override
    public String getProvider() {
        return "google";
    }

    @Override
    public String getProviderId() {
        return attributes.get("sub").toString();
    }

    @Override
    public UserEntity toUserEntity() {
        return UserEntity.builder()
            .oauth2UserId(getOAuth2UserId())
            .username(attributes.get("name").toString())
            .build();
    }

}
