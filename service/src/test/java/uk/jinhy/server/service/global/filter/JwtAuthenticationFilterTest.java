package uk.jinhy.server.service.global.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpHeaders;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import uk.jinhy.server.api.auth.jwt.application.JwtService;
import uk.jinhy.server.util.jwt.JwtProvider;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static uk.jinhy.server.util.jwt.JwtConst.*;

@ExtendWith(MockitoExtension.class)
class JwtAuthenticationFilterTest {

    @Mock
    private JwtService jwtService;

    @Mock
    private HttpServletRequest request;

    @Mock
    private HttpServletResponse response;

    @Mock
    private FilterChain filterChain;

    @Mock
    private SecurityContext securityContext;

    @InjectMocks
    private JwtAuthenticationFilter jwtAuthenticationFilter;

    @BeforeEach
    void setUp() {
        SecurityContextHolder.setContext(securityContext);
    }

    @Test
    void doFilterInternal_NoTokens_ShouldContinueChain() throws Exception {
        // Given
        when(request.getHeader(HttpHeaders.AUTHORIZATION)).thenReturn(null);
        when(request.getCookies()).thenReturn(null);

        // When
        jwtAuthenticationFilter.doFilterInternal(request, response, filterChain);

        // Then
        verify(filterChain).doFilter(request, response);
        verify(securityContext, never()).setAuthentication(any(Authentication.class));
    }

    @Test
    void doFilterInternal_ValidAccessToken_ShouldAuthenticate() throws Exception {
        // Given
        String validToken = "valid.access.token";
        try (var jwtProviderMock = mockStatic(JwtProvider.class)) {
            jwtProviderMock.when(() -> JwtProvider.isValid(validToken)).thenReturn(true);
            jwtProviderMock.when(() -> JwtProvider.isExpired(validToken)).thenReturn(false);
            jwtProviderMock.when(() -> JwtProvider.parseSubject(validToken)).thenReturn("user123");

            when(request.getHeader(HttpHeaders.AUTHORIZATION)).thenReturn(BEARER + validToken);

            // When
            jwtAuthenticationFilter.doFilterInternal(request, response, filterChain);

            // Then
            verify(securityContext).setAuthentication(any(Authentication.class));
            verify(filterChain).doFilter(request, response);
        }
    }

    @Test
    void doFilterInternal_InvalidAccessToken_ShouldReturnBadRequest() throws Exception {
        // Given
        String invalidToken = "invalid.access.token";
        try (var jwtProviderMock = mockStatic(JwtProvider.class)) {
            jwtProviderMock.when(() -> JwtProvider.isValid(invalidToken)).thenReturn(false);

            when(request.getHeader(HttpHeaders.AUTHORIZATION)).thenReturn(BEARER + invalidToken);

            // When
            jwtAuthenticationFilter.doFilterInternal(request, response, filterChain);

            // Then
            verify(response).setStatus(HttpServletResponse.SC_BAD_REQUEST);
            verify(securityContext, never()).setAuthentication(any(Authentication.class));
        }
    }

    @Test
    void doFilterInternal_ExpiredAccessToken_ShouldRedirectToRefresh() throws Exception {
        // Given
        String expiredToken = "expired.access.token";
        try (var jwtProviderMock = mockStatic(JwtProvider.class)) {
            jwtProviderMock.when(() -> JwtProvider.isValid(expiredToken)).thenReturn(true);
            jwtProviderMock.when(() -> JwtProvider.isExpired(expiredToken)).thenReturn(true);

            when(request.getHeader(HttpHeaders.AUTHORIZATION)).thenReturn(BEARER + expiredToken);

            // When
            jwtAuthenticationFilter.doFilterInternal(request, response, filterChain);

            // Then
            verify(response).sendRedirect(JWT_REFRESH_TOKEN_COOKIE_PATH);
            verify(securityContext, never()).setAuthentication(any(Authentication.class));
        }
    }

    @Test
    void doFilterInternal_ValidRefreshToken_ShouldContinueChain() throws Exception {
        // Given
        String validRefreshToken = "valid.refresh.token";
        Cookie refreshCookie = new Cookie(JWT_REFRESH_TOKEN_COOKIE_NAME, validRefreshToken);
        Cookie[] cookies = {refreshCookie};

        try (var jwtProviderMock = mockStatic(JwtProvider.class)) {
            jwtProviderMock.when(() -> JwtProvider.isValid(validRefreshToken)).thenReturn(true);
            jwtProviderMock.when(() -> JwtProvider.isExpired(validRefreshToken)).thenReturn(false);

            when(request.getHeader(HttpHeaders.AUTHORIZATION)).thenReturn(null);
            when(request.getCookies()).thenReturn(cookies);
            when(jwtService.isBlackListed(validRefreshToken)).thenReturn(false);

            // When
            jwtAuthenticationFilter.doFilterInternal(request, response, filterChain);

            // Then
            verify(filterChain).doFilter(request, response);
        }
    }

    @Test
    void doFilterInternal_InvalidRefreshToken_ShouldReturnBadRequest() throws Exception {
        // Given
        String invalidRefreshToken = "invalid.refresh.token";
        Cookie refreshCookie = new Cookie(JWT_REFRESH_TOKEN_COOKIE_NAME, invalidRefreshToken);
        Cookie[] cookies = {refreshCookie};

        try (var jwtProviderMock = mockStatic(JwtProvider.class)) {
            jwtProviderMock.when(() -> JwtProvider.isValid(invalidRefreshToken)).thenReturn(false);

            when(request.getHeader(HttpHeaders.AUTHORIZATION)).thenReturn(null);
            when(request.getCookies()).thenReturn(cookies);

            // When
            jwtAuthenticationFilter.doFilterInternal(request, response, filterChain);

            // Then
            verify(response).setStatus(HttpServletResponse.SC_BAD_REQUEST);
        }
    }

    @Test
    void doFilterInternal_BlacklistedRefreshToken_ShouldReturnBadRequest() throws Exception {
        // Given
        String blacklistedToken = "blacklisted.refresh.token";
        Cookie refreshCookie = new Cookie(JWT_REFRESH_TOKEN_COOKIE_NAME, blacklistedToken);
        Cookie[] cookies = {refreshCookie};

        try (var jwtProviderMock = mockStatic(JwtProvider.class)) {
            jwtProviderMock.when(() -> JwtProvider.isValid(blacklistedToken)).thenReturn(true);

            when(request.getHeader(HttpHeaders.AUTHORIZATION)).thenReturn(null);
            when(request.getCookies()).thenReturn(cookies);
            when(jwtService.isBlackListed(blacklistedToken)).thenReturn(true);

            // When
            jwtAuthenticationFilter.doFilterInternal(request, response, filterChain);

            // Then
            verify(response).setStatus(HttpServletResponse.SC_BAD_REQUEST);
        }
    }

    @Test
    void doFilterInternal_ExpiredRefreshToken_ShouldRedirectToLogin() throws Exception {
        // Given
        String expiredRefreshToken = "expired.refresh.token";
        Cookie refreshCookie = new Cookie(JWT_REFRESH_TOKEN_COOKIE_NAME, expiredRefreshToken);
        Cookie[] cookies = {refreshCookie};

        try (var jwtProviderMock = mockStatic(JwtProvider.class)) {
            jwtProviderMock.when(() -> JwtProvider.isValid(expiredRefreshToken)).thenReturn(true);
            jwtProviderMock.when(() -> JwtProvider.isExpired(expiredRefreshToken)).thenReturn(true);

            when(request.getHeader(HttpHeaders.AUTHORIZATION)).thenReturn(null);
            when(request.getCookies()).thenReturn(cookies);
            when(jwtService.isBlackListed(expiredRefreshToken)).thenReturn(false);

            // When
            jwtAuthenticationFilter.doFilterInternal(request, response, filterChain);

            // Then
            verify(response).sendRedirect("/api/auth/login");
        }
    }
}
