package uk.jinhy.server.service.auth.jwt.presentation;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.RestController;
import uk.jinhy.server.api.auth.jwt.application.JwtService;
import uk.jinhy.server.api.auth.jwt.dto.JwtTokenDTO;
import uk.jinhy.server.api.auth.jwt.presentation.JwtController;

import java.util.Arrays;

import static uk.jinhy.server.util.jwt.JwtConst.*;

@Slf4j
@RequiredArgsConstructor
@RestController
public class JwtControllerImpl implements JwtController {

    private final JwtService jwtService;

    @Override
    public ResponseEntity<Void> reissueTokens(
        HttpServletRequest request,
        HttpServletResponse response,
        @CookieValue(JWT_REFRESH_TOKEN_COOKIE_NAME) String token
    ) {
        log.info("oldToken: {}", token);
        JwtTokenDTO jwtTokenDTO = jwtService.reissueTokens(token);

        String accessToken = jwtTokenDTO.getAccessToken();
        response.setHeader(HttpHeaders.AUTHORIZATION, BEARER + accessToken);

        String refreshToken = jwtTokenDTO.getRefreshToken();
        response.addCookie(generateRefreshTokenCookie(refreshToken));

        log.info("new accessToken: {}", accessToken);
        log.info("new refreshToken: {}", refreshToken);

        return ResponseEntity.ok().build();
    }

    @Override
    public ResponseEntity<Void> logout(
        HttpServletRequest request,
        HttpServletResponse response,
        @CookieValue(JWT_REFRESH_TOKEN_COOKIE_NAME) String refreshToken
    ) {
        jwtService.logout(refreshToken);
        deleteCookie(request, response, JWT_REFRESH_TOKEN_COOKIE_NAME);

        return ResponseEntity.ok().build();
    }

    private Cookie generateRefreshTokenCookie(String token) {
        Cookie cookie = new Cookie(JWT_REFRESH_TOKEN_COOKIE_NAME, token);
        cookie.setHttpOnly(true);
        cookie.setSecure(false); //운영환경에서 true
        cookie.setPath(JWT_REFRESH_TOKEN_COOKIE_PATH);
        cookie.setMaxAge((int) JWT_REFRESH_TOKEN_EXPIRATION_TIME);
        return cookie;
    }

    private void deleteCookie(HttpServletRequest request, HttpServletResponse response, String name) {
        Arrays.stream(request.getCookies())
            .filter(cookie -> cookie.getName().equals(name))
            .findFirst()
            .ifPresent(cookie -> {
                cookie.setMaxAge(0);
                cookie.setPath("/");

                response.addCookie(cookie);
                response.setStatus(HttpServletResponse.SC_OK);
            });
    }
}
