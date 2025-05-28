package uk.jinhy.server.service.user.oauth2;

import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.function.Function;

public class OAuth2UserResponseFactory {

    public static OAuth2UserResponse create(OAuth2User oAuth2User, String registrationId) {
        return getInstance(registrationId).apply(oAuth2User);
    }

    private static Function<OAuth2User, OAuth2UserResponse> getInstance(String registrationId) {
        if (registrationId.equals("google")) {
            return GoogleOAuth2UserResponse::new;
        }
        if (registrationId.equals("kakao")) {
            return KakaoOAuth2UserResponse::new;
        }
        throw new IllegalArgumentException("Unsupported registration id " + registrationId);
    }
}
