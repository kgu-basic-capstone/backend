package uk.jinhy.server.service.global.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import uk.jinhy.server.api.auth.jwt.application.JwtService;
import uk.jinhy.server.util.jwt.JwtProvider;

import java.io.IOException;

import static org.mockito.Mockito.*;
import static uk.jinhy.server.util.jwt.JwtConst.JWT_REFRESH_TOKEN_COOKIE_NAME;

@ExtendWith(MockitoExtension.class)
class JwtLogoutFilterTest {

    @Mock
    private JwtService jwtService;

    @Mock
    private HttpServletRequest request;

    @Mock
    private HttpServletResponse response;

    @Mock
    private FilterChain filterChain;

    @InjectMocks
    private JwtLogoutFilter jwtLogoutFilter;

    @Test
    void doFilter_NotLogoutEndpoint_ShouldContinueChain() throws ServletException, IOException {
        // Given
        when(request.getRequestURI()).thenReturn("/api/some-other-endpoint");

        // When
        jwtLogoutFilter.doFilter(request, response, filterChain);

        // Then
        verify(filterChain).doFilter(request, response);
        verifyNoInteractions(jwtService);
    }

    @Test
    void doFilter_LogoutEndpointNotPostMethod_ShouldContinueChain() throws ServletException, IOException {
        // Given
        when(request.getRequestURI()).thenReturn("/api/auth/logout");
        when(request.getMethod()).thenReturn("GET");

        // When
        jwtLogoutFilter.doFilter(request, response, filterChain);

        // Then
        verify(filterChain).doFilter(request, response);
        verifyNoInteractions(jwtService);
    }

    @Test
    void doFilter_LogoutEndpointWithNoRefreshToken_ShouldReturnBadRequest() throws ServletException, IOException {
        // Given
        when(request.getRequestURI()).thenReturn("/api/auth/logout");
        when(request.getMethod()).thenReturn("POST");
        when(request.getCookies()).thenReturn(null);

        // When
        jwtLogoutFilter.doFilter(request, response, filterChain);

        // Then
        verify(response).setStatus(HttpServletResponse.SC_BAD_REQUEST);
        verifyNoInteractions(filterChain);
    }

    @Test
    void doFilter_LogoutEndpointWithBlacklistedToken_ShouldReturnBadRequest() throws ServletException, IOException {
        // Given
        String blacklistedToken = "blacklisted.refresh.token";
        Cookie refreshCookie = new Cookie(JWT_REFRESH_TOKEN_COOKIE_NAME, blacklistedToken);
        Cookie[] cookies = {refreshCookie};

        when(request.getRequestURI()).thenReturn("/api/auth/logout");
        when(request.getMethod()).thenReturn("POST");
        when(request.getCookies()).thenReturn(cookies);
        when(jwtService.isBlackListed(blacklistedToken)).thenReturn(true);

        // When
        jwtLogoutFilter.doFilter(request, response, filterChain);

        // Then
        verify(response).setStatus(HttpServletResponse.SC_BAD_REQUEST);
        verifyNoInteractions(filterChain);
    }

    @Test
    void doFilter_LogoutEndpointWithExpiredToken_ShouldReturnBadRequest() throws ServletException, IOException {
        // Given
        String expiredToken = "expired.refresh.token";
        Cookie refreshCookie = new Cookie(JWT_REFRESH_TOKEN_COOKIE_NAME, expiredToken);
        Cookie[] cookies = {refreshCookie};

        try (var jwtProviderMock = mockStatic(JwtProvider.class)) {
            jwtProviderMock.when(() -> JwtProvider.isExpired(expiredToken)).thenReturn(true);

            when(request.getRequestURI()).thenReturn("/api/auth/logout");
            when(request.getMethod()).thenReturn("POST");
            when(request.getCookies()).thenReturn(cookies);
            when(jwtService.isBlackListed(expiredToken)).thenReturn(false);

            // When
            jwtLogoutFilter.doFilter(request, response, filterChain);

            // Then
            verify(response).setStatus(HttpServletResponse.SC_BAD_REQUEST);
            verifyNoInteractions(filterChain);
        }
    }

    @Test
    void doFilter_LogoutEndpointWithValidToken_ShouldContinueChain() throws ServletException, IOException {
        // Given
        String validToken = "valid.refresh.token";
        Cookie refreshCookie = new Cookie(JWT_REFRESH_TOKEN_COOKIE_NAME, validToken);
        Cookie[] cookies = {refreshCookie};

        try (var jwtProviderMock = mockStatic(JwtProvider.class)) {
            jwtProviderMock.when(() -> JwtProvider.isExpired(validToken)).thenReturn(false);

            when(request.getRequestURI()).thenReturn("/api/auth/logout");
            when(request.getMethod()).thenReturn("POST");
            when(request.getCookies()).thenReturn(cookies);
            when(jwtService.isBlackListed(validToken)).thenReturn(false);

            // When
            jwtLogoutFilter.doFilter(request, response, filterChain);

            // Then
            verify(filterChain).doFilter(request, response);
        }
    }
}
