package uk.jinhy.server.api.auth.jwt.dto;

import lombok.Getter;

@Getter
public class JwtTokenDTO {

    private final String accessToken;
    private final String refreshToken;

    public JwtTokenDTO(String accessToken, String refreshToken) {
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
    }

    public static JwtTokenDTO of(String accessToken, String refreshToken) {
        return new JwtTokenDTO(accessToken, refreshToken);
    }

}
