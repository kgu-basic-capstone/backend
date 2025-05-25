package uk.jinhy.server.service.auth.jwt.application;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import uk.jinhy.server.api.auth.jwt.dto.JwtTokenDTO;
import uk.jinhy.server.util.jwt.JwtProvider;

import java.time.Duration;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class JwtServiceImplTest {

    @Mock
    private StringRedisTemplate redisTemplate;

    @Mock
    private ValueOperations<String, String> valueOperations;

    @InjectMocks
    private JwtServiceImpl jwtService;

    private static final String BLACKLIST_PREFIX = "blacklist:";
    private static final String TEST_REFRESH_TOKEN = "test.refresh.token";
    private static final String TEST_USER_ID = "test-user-123";
    private static final String TEST_ACCESS_TOKEN = "test.access.token";
    private static final long TEST_EXPIRATION_TIME = 3600000L;

    @BeforeEach
    public void setUp() {
        lenient().when(redisTemplate.opsForValue()).thenReturn(valueOperations);
    }

    @Test
    void reissueTokens_ShouldGenerateNewTokensAndBlacklistOldToken() {
        // Given
        try (var jwtProviderMock = mockStatic(JwtProvider.class)) {
            jwtProviderMock.when(() -> JwtProvider.parseSubject(TEST_REFRESH_TOKEN)).thenReturn(TEST_USER_ID);
            jwtProviderMock.when(() -> JwtProvider.generateAccessToken(TEST_USER_ID)).thenReturn(TEST_ACCESS_TOKEN);
            jwtProviderMock.when(() -> JwtProvider.generateRefreshToken(TEST_USER_ID)).thenReturn(TEST_REFRESH_TOKEN + "_new");
            jwtProviderMock.when(() -> JwtProvider.getRemainExpirationTime(TEST_REFRESH_TOKEN)).thenReturn(TEST_EXPIRATION_TIME);

            // When
            JwtTokenDTO result = jwtService.reissueTokens(TEST_REFRESH_TOKEN);

            // Then
            assertEquals(TEST_ACCESS_TOKEN, result.getAccessToken());
            assertEquals(TEST_REFRESH_TOKEN + "_new", result.getRefreshToken());
            verify(valueOperations).set(
                eq(BLACKLIST_PREFIX + TEST_REFRESH_TOKEN),
                eq("blacklisted"),
                eq(Duration.ofMillis(TEST_EXPIRATION_TIME))
            );
        }
    }

    @Test
    void addToBlackList_ShouldAddTokenToRedis() {
        // Given
        try (var jwtProviderMock = mockStatic(JwtProvider.class)) {
            jwtProviderMock.when(() -> JwtProvider.getRemainExpirationTime(TEST_REFRESH_TOKEN)).thenReturn(TEST_EXPIRATION_TIME);

            // When
            jwtService.addToBlackList(TEST_REFRESH_TOKEN);

            // Then
            verify(valueOperations).set(
                eq(BLACKLIST_PREFIX + TEST_REFRESH_TOKEN),
                eq("blacklisted"),
                eq(Duration.ofMillis(TEST_EXPIRATION_TIME))
            );
        }
    }

    @Test
    void logout_ShouldCallAddToBlackList() {
        // Given
        doNothing().when(valueOperations).set(anyString(), anyString(), any(Duration.class));
        try (var jwtProviderMock = mockStatic(JwtProvider.class)) {
            jwtProviderMock.when(() -> JwtProvider.getRemainExpirationTime(TEST_REFRESH_TOKEN)).thenReturn(TEST_EXPIRATION_TIME);

            // When
            jwtService.logout(TEST_REFRESH_TOKEN);

            // Then
            verify(valueOperations).set(
                eq(BLACKLIST_PREFIX + TEST_REFRESH_TOKEN),
                eq("blacklisted"),
                eq(Duration.ofMillis(TEST_EXPIRATION_TIME))
            );
        }
    }

    @Test
    void isBlackListed_TokenExists_ShouldReturnTrue() {
        // Given
        when(redisTemplate.hasKey(BLACKLIST_PREFIX + TEST_REFRESH_TOKEN)).thenReturn(true);

        // When
        boolean result = jwtService.isBlackListed(TEST_REFRESH_TOKEN);

        // Then
        assertTrue(result);
    }

    @Test
    void isBlackListed_TokenDoesNotExist_ShouldReturnFalse() {
        // Given
        when(redisTemplate.hasKey(BLACKLIST_PREFIX + TEST_REFRESH_TOKEN)).thenReturn(false);

        // When
        boolean result = jwtService.isBlackListed(TEST_REFRESH_TOKEN);

        // Then
        assertFalse(result);
    }
}
