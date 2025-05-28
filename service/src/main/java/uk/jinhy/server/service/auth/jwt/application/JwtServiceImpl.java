package uk.jinhy.server.service.auth.jwt.application;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import uk.jinhy.server.api.auth.jwt.application.JwtService;
import uk.jinhy.server.api.auth.jwt.dto.JwtTokenDTO;
import uk.jinhy.server.util.jwt.JwtProvider;

import java.time.Duration;

@Slf4j
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class JwtServiceImpl implements JwtService {

    private final static String BLACKLIST_PREFIX = "blacklist:";

    private final StringRedisTemplate redisTemplate;

    @Transactional
    @Override
    public JwtTokenDTO reissueTokens(String oldToken) {
        String oauth2UserId = JwtProvider.parseSubject(oldToken);

        String accessToken = JwtProvider.generateAccessToken(oauth2UserId);
        String refreshToken = JwtProvider.generateRefreshToken(oauth2UserId);

        addToBlackList(oldToken);

        return JwtTokenDTO.of(accessToken, refreshToken);
    }

    @Transactional
    @Override
    public void addToBlackList(String refreshToken) {
        redisTemplate.opsForValue()
            .set(
                BLACKLIST_PREFIX + refreshToken,
                "blacklisted",
                Duration.ofMillis(JwtProvider.getRemainExpirationTime(refreshToken))
            );

        log.info("blacklisted: {}", refreshToken);
    }

    @Transactional
    @Override
    public void logout(String refreshToken) {
        addToBlackList(refreshToken);
    }

    @Override
    public boolean isBlackListed(String refreshToken) {
        return redisTemplate.hasKey(BLACKLIST_PREFIX + refreshToken);
    }
}
