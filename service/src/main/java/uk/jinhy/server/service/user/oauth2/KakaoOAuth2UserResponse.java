package uk.jinhy.server.service.user.oauth2;

import org.springframework.security.oauth2.core.user.OAuth2User;
import uk.jinhy.server.service.user.domain.UserEntity;

import java.util.Map;

public class KakaoOAuth2UserResponse implements OAuth2UserResponse {

    private final Map<String, Object> attributes;

    public KakaoOAuth2UserResponse(OAuth2User oAuth2User) {
        this.attributes = oAuth2User.getAttributes();
    }

    @Override
    public Map<String, Object> getAttributes() {
        return Map.copyOf(attributes);
    }

    @Override
    public String getProvider() {
        return "kakao";
    }

    @Override
    public String getProviderId() {
        return attributes.get("id").toString();
    }

    @Override
    public UserEntity toUserEntity() {
        return UserEntity.builder()
            .oauth2UserId(getOAuth2UserId())
            .username(getUsername())
            .build();
    }

    @SuppressWarnings("unchecked")
    private String getUsername() {
        Map<String, Object> kakaoAccount = (Map<String, Object>) attributes.get("kakao_account");
        Map<String, Object> profile = (Map<String, Object>) kakaoAccount.get("profile");
        return profile.get("nickname").toString();
    }

}
