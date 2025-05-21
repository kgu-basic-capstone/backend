package uk.jinhy.server.service.global.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.GenericFilterBean;
import uk.jinhy.server.api.auth.jwt.application.JwtService;
import uk.jinhy.server.util.jwt.JwtProvider;

import java.io.IOException;
import java.util.Arrays;

import static uk.jinhy.server.util.jwt.JwtConst.JWT_REFRESH_TOKEN_COOKIE_NAME;

@Slf4j
@RequiredArgsConstructor
@Component
public class JwtLogoutFilter extends GenericFilterBean {

    private final JwtService jwtService;

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest httpServletRequest = (HttpServletRequest) request;

        String requestURI = getRequestURI(httpServletRequest);
        if (!requestURI.contains("/logout")) {
            chain.doFilter(request, response);
            return;
        }

        String requestMethod = getRequestMethod(httpServletRequest);
        if (!requestMethod.equals("POST")) {
            chain.doFilter(request, response);
            return;
        }

        HttpServletResponse httpServletResponse = (HttpServletResponse) response;

        String refreshToken = getRefreshToken(httpServletRequest);
        if (refreshToken == null) {
            httpServletResponse.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }
        if (jwtService.isBlackListed(refreshToken)) {
            httpServletResponse.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }
        if (JwtProvider.isExpired(refreshToken)) {
            httpServletResponse.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }

        log.info("logout refresh token: {}", refreshToken);
        chain.doFilter(request, response);
    }

    private String getRequestURI(HttpServletRequest request) {
        return request.getRequestURI();
    }

    private String getRequestMethod(HttpServletRequest request) {
        return request.getMethod();
    }

    private String getRefreshToken(HttpServletRequest request) {
        Cookie cookie = getCookie(request, JWT_REFRESH_TOKEN_COOKIE_NAME);
        return (cookie == null) ? null : cookie.getValue();
    }

    private Cookie getCookie(HttpServletRequest request, String name) {
        Cookie[] cookies = request.getCookies();
        if (cookies == null) {
            return null;
        }

        return Arrays.stream(cookies)
            .filter(cookie -> name.equalsIgnoreCase(cookie.getName()))
            .findAny()
            .orElse(null);
    }
}
