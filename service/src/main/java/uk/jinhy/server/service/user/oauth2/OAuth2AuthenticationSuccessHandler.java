package uk.jinhy.server.service.user.oauth2;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import uk.jinhy.server.util.jwt.JwtProvider;

import java.io.IOException;

import static uk.jinhy.server.util.jwt.JwtConst.*;

@Slf4j
@Component
public class OAuth2AuthenticationSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException {
        CustomOAuth2User customOAuth2User = (CustomOAuth2User) authentication.getPrincipal();

        String refreshToken = JwtProvider.generateRefreshToken(customOAuth2User.getName());
        response.addCookie(generateCookie(refreshToken));

        log.info("successHandler refreshToken: {}", refreshToken);
        getRedirectStrategy().sendRedirect(request, response, "/api/auth/reissue");
    }

    private Cookie generateCookie(String token) {
        Cookie cookie = new Cookie(JWT_REFRESH_TOKEN_COOKIE_NAME, token);
        cookie.setHttpOnly(true);
        cookie.setSecure(false); //운영환경에서 true
        cookie.setPath(JWT_REFRESH_TOKEN_COOKIE_PATH);
        cookie.setMaxAge((int) JWT_REFRESH_TOKEN_EXPIRATION_TIME);
        return cookie;
    }
}
