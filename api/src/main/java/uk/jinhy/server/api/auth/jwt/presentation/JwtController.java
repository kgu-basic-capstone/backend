package uk.jinhy.server.api.auth.jwt.presentation;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import java.io.IOException;

@Tag(name = "JWT", description = "JWT 토큰 API")
public interface JwtController {

    @Operation(
        summary = "액세스, 리프레시 토큰 발급",
        description = "인가에 사용될 액세스 토큰, 만료된 액세스 토큰을 재발급 하는데 사용될 리프레시 토큰을 발급합니다."
    )
    @GetMapping("/api/auth/reissue")
    ResponseEntity<Void> reissueTokens(HttpServletRequest request, HttpServletResponse response, String refreshToken) throws IOException;

    @Operation(
        summary = "로그아웃",
        description = "refresh token을 만료 시킵니다."
    )
    @PostMapping("/api/auth/logout")
    ResponseEntity<Void> logout(
        HttpServletRequest request,
        HttpServletResponse response,
        String token
    ) throws IOException;
}
