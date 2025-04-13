package uk.jinhy.server.api.auth.jwt.application;

import uk.jinhy.server.api.auth.jwt.dto.JwtTokenDTO;

public interface JwtService {

    JwtTokenDTO reissueTokens(String refreshToken);

    void addToBlackList(String token);

    boolean isBlackListed(String token);

    void logout(String refreshToken);

}
