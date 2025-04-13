package uk.jinhy.server.service.global.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import uk.jinhy.server.api.auth.jwt.application.JwtService;
import uk.jinhy.server.util.jwt.JwtProvider;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import static uk.jinhy.server.util.jwt.JwtConst.*;

@Slf4j
@RequiredArgsConstructor
@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtService jwtService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String accessToken = getAccessToken(request);
        String refreshToken = getRefreshToken(request);

        if (accessToken == null && refreshToken == null) {
            log.info("no token");
            filterChain.doFilter(request, response);
            return;
        }

        if (accessToken != null && !JwtProvider.isValid(accessToken)) {
            log.info("Invalid access token");
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        }
        if (accessToken != null && JwtProvider.isExpired(accessToken)) {
            log.info("Expired access token");
            response.sendRedirect(JWT_REFRESH_TOKEN_COOKIE_PATH);
            return;
        }

        if (accessToken != null) {
            Authentication authentication = createAuthentication(accessToken);
            SecurityContextHolder.getContext().setAuthentication(authentication);

            log.info("Authentication successful, access token: {}", accessToken);
            filterChain.doFilter(request, response);
            return;
        }

        if (!JwtProvider.isValid(refreshToken)) {
            log.info("Invalid refresh token");
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }
        if (jwtService.isBlackListed(refreshToken)) {
            log.info("Blacklisted refresh token");
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }
        if (JwtProvider.isExpired(refreshToken)) {
            log.info("Refresh token expired");
            deleteCookie(request, response, JWT_REFRESH_TOKEN_COOKIE_NAME);
            response.sendRedirect("/api/auth/login");
            return;
        }

        filterChain.doFilter(request, response);
    }

    private String getAccessToken(HttpServletRequest request) {
        String header = request.getHeader(HttpHeaders.AUTHORIZATION);
        return (header == null || !header.startsWith(BEARER)) ? null : header.split(" ")[1];
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

    private UsernamePasswordAuthenticationToken createAuthentication(String accessToken) {
        String oAuth2UserId = JwtProvider.parseSubject(accessToken);
        Collection<GrantedAuthority> authorities = List.of(new SimpleGrantedAuthority("ROLE_USER"));
        return new UsernamePasswordAuthenticationToken(oAuth2UserId, accessToken, authorities);
    }

}
