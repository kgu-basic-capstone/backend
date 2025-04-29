package uk.jinhy.server.service.auth.jwt.presentation;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import uk.jinhy.server.api.auth.jwt.application.JwtService;
import uk.jinhy.server.api.auth.jwt.dto.JwtTokenDTO;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;
import static uk.jinhy.server.util.jwt.JwtConst.BEARER;
import static uk.jinhy.server.util.jwt.JwtConst.JWT_REFRESH_TOKEN_COOKIE_NAME;

@ExtendWith(MockitoExtension.class)
class JwtControllerImplTest {

    @Mock
    private JwtService jwtService;

    @Mock
    private HttpServletRequest request;

    @Mock
    private HttpServletResponse response;

    @InjectMocks
    private JwtControllerImpl jwtController;

    private static final String TEST_REFRESH_TOKEN = "test.refresh.token";
    private static final String TEST_ACCESS_TOKEN = "test.access.token";
    private static final String TEST_NEW_REFRESH_TOKEN = "test.new.refresh.token";

    @Test
    void reissueTokens_ShouldSetHeadersAndCookies() {
        // Given
        JwtTokenDTO jwtTokenDTO = new JwtTokenDTO(TEST_ACCESS_TOKEN, TEST_NEW_REFRESH_TOKEN);
        when(jwtService.reissueTokens(TEST_REFRESH_TOKEN)).thenReturn(jwtTokenDTO);

        // When
        ResponseEntity<Void> response = jwtController.reissueTokens(request, this.response, TEST_REFRESH_TOKEN);

        // Then
        assertEquals(200, response.getStatusCodeValue());
        verify(this.response).setHeader(HttpHeaders.AUTHORIZATION, BEARER + TEST_ACCESS_TOKEN);
        verify(this.response).addCookie(any(Cookie.class));
        verify(jwtService).reissueTokens(TEST_REFRESH_TOKEN);
    }

    @Test
    void logout_ShouldCallServiceAndDeleteCookie() {
        // Given
        Cookie refreshCookie = new Cookie(JWT_REFRESH_TOKEN_COOKIE_NAME, TEST_REFRESH_TOKEN);
        Cookie[] cookies = {refreshCookie};
        when(request.getCookies()).thenReturn(cookies);

        // When
        ResponseEntity<Void> response = jwtController.logout(request, this.response, TEST_REFRESH_TOKEN);

        // Then
        assertEquals(200, response.getStatusCodeValue());
        verify(jwtService).logout(TEST_REFRESH_TOKEN);
        verify(this.response).addCookie(any(Cookie.class));
        verify(this.response).setStatus(HttpServletResponse.SC_OK);
    }

    @Test
    void logout_NoCookies_ShouldStillCallService() {
        // Given
        when(request.getCookies()).thenReturn(new Cookie[0]);

        // When
        ResponseEntity<Void> response = jwtController.logout(request, this.response, TEST_REFRESH_TOKEN);

        // Then
        assertEquals(200, response.getStatusCodeValue());
        verify(jwtService).logout(TEST_REFRESH_TOKEN);
        // Cookie deletion not attempted since there are no cookies
        verify(this.response, never()).addCookie(any(Cookie.class));
    }
}
