package uk.jinhy.server.api.user.presentation;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import uk.jinhy.server.api.user.presentation.dto.response.AuthenticatedUserInfoResponseDto;

@Tag(name = "User", description = "회원 관리 API")
public interface UserController {

    @Operation(
        summary = "회원 정보 조회",
        description = "로그인 시 발급 받은 JWT 토큰으로 회원 정보를 조회합니다.",
        responses = {
            @ApiResponse(responseCode = "200", description = "회원 정보 조회 성공")
        }
    )
    @GetMapping("/api/users/me")
    ResponseEntity<AuthenticatedUserInfoResponseDto> getAuthUserInfo(String oauth2UserId);

}
