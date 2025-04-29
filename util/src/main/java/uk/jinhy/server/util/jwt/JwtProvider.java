package uk.jinhy.server.util.jwt;

import io.jsonwebtoken.Jwts;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.UUID;

@Component
public class JwtProvider {

    private static final SecretKey secretKey = Jwts.SIG.HS256.key().build();

    public static String generateAccessToken(String oAuth2UserId) {
        return Jwts.builder()
            .issuer(JwtConst.JWT_ISSUER)
            .subject(oAuth2UserId)
            .issuedAt(new Date(System.currentTimeMillis()))
            .expiration(new Date(System.currentTimeMillis() + JwtConst.JWT_ACCESS_TOKEN_EXPIRATION_TIME))
            .id(UUID.randomUUID().toString())
            .signWith(secretKey)
            .compact();
    }

    public static String generateRefreshToken(String oAuth2UserId) {
        return Jwts.builder()
            .issuer(JwtConst.JWT_ISSUER)
            .subject(oAuth2UserId)
            .issuedAt(new Date(System.currentTimeMillis()))
            .expiration(new Date(System.currentTimeMillis() + JwtConst.JWT_REFRESH_TOKEN_EXPIRATION_TIME))
            .id(UUID.randomUUID().toString())
            .signWith(secretKey)
            .compact();
    }

    public static boolean isValid(String token) {
        try {
            Jwts.parser()
                .verifyWith(secretKey)
                .build()
                .parseSignedClaims(token);

            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public static boolean isExpired(String token) {
        try {
            return Jwts.parser()
                .verifyWith(secretKey)
                .build()
                .parseSignedClaims(token)
                .getPayload()
                .getExpiration()
                .before(new Date(System.currentTimeMillis()));
        } catch (Exception e) {
            return false;
        }
    }

    public static String parseSubject(String token) {
        return Jwts.parser()
            .verifyWith(secretKey)
            .build()
            .parseSignedClaims(token)
            .getPayload()
            .getSubject();
    }

    public static long getRemainExpirationTime(String token) {
        Date expiration = getExpirationTime(token);
        return expiration.getTime() - System.currentTimeMillis();
    }

    private static Date getExpirationTime(String token) {
        return Jwts.parser()
            .verifyWith(secretKey)
            .build()
            .parseSignedClaims(token)
            .getPayload()
            .getExpiration();
    }

}
