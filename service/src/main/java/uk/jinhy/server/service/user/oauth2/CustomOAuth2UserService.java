package uk.jinhy.server.service.user.oauth2;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import uk.jinhy.server.service.user.domain.UserEntityRepository;

@Slf4j
@Transactional
@RequiredArgsConstructor
@Service
public class CustomOAuth2UserService extends DefaultOAuth2UserService {

    private final UserEntityRepository userEntityRepository;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2User oAuth2User = super.loadUser(userRequest);

        String registrationId = getRegistrationId(userRequest);
        OAuth2UserResponse oAuth2UserResponse = OAuth2UserResponseFactory.create(oAuth2User, registrationId);

        String oAuth2UserId = oAuth2UserResponse.getOAuth2UserId();
        userEntityRepository.findByOauth2UserId(oAuth2UserId)
            .orElseGet(() -> userEntityRepository.save(oAuth2UserResponse.toUserEntity()));

        log.info("oAuth2UserId: {}", oAuth2UserResponse.getOAuth2UserId());
        return new CustomOAuth2User(oAuth2UserId, oAuth2UserResponse.getAttributes());
    }

    private String getRegistrationId(OAuth2UserRequest userRequest) {
        return userRequest.getClientRegistration().getRegistrationId();
    }

}
