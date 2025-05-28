package uk.jinhy.server.service.user.oauth2;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.RedirectStrategy;
import uk.jinhy.server.util.jwt.JwtProvider;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class OAuth2AuthenticationSuccessHandlerTest {

    @Mock
    private RedirectStrategy redirectStrategy;

    @Mock
    private HttpServletRequest request;

    @Mock
    private HttpServletResponse response;

    @Mock
    private Authentication authentication;

    @InjectMocks
    private OAuth2AuthenticationSuccessHandler successHandler;

    @Test
    void onAuthenticationSuccess_ShouldGenerateTokenAndRedirect() throws IOException {
        // Given
        String oAuth2UserId = "oauth2-user-123";
        String refreshToken = "test.refresh.token";

        Map<String, Object> attributes = new HashMap<>();
        attributes.put("sub", oAuth2UserId);

        CustomOAuth2User customOAuth2User = new CustomOAuth2User(oAuth2UserId, attributes);

        when(authentication.getPrincipal()).thenReturn(customOAuth2User);

        try (var jwtProviderMock = mockStatic(JwtProvider.class)) {
            jwtProviderMock.when(() -> JwtProvider.generateRefreshToken(oAuth2UserId)).thenReturn(refreshToken);

            // Set up the redirect strategy in the handler
            successHandler.setRedirectStrategy(redirectStrategy);

            // When
            successHandler.onAuthenticationSuccess(request, response, authentication);

            // Then
            verify(response).addCookie(any(Cookie.class));
            verify(redirectStrategy).sendRedirect(eq(request), eq(response), eq("/api/auth/reissue"));
            jwtProviderMock.verify(() -> JwtProvider.generateRefreshToken(oAuth2UserId));
        }
    }
}
