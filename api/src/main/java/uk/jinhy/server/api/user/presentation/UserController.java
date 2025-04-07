package uk.jinhy.server.api.user.presentation;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import uk.jinhy.server.api.user.presentation.UserDto.LoginRequest;
import uk.jinhy.server.api.user.presentation.UserDto.LoginResponse;
import uk.jinhy.server.api.user.presentation.UserDto.SignupRequest;
import uk.jinhy.server.api.user.presentation.UserDto.UpdateProfileRequest;
import uk.jinhy.server.api.user.presentation.UserDto.UserResponse;

@Tag(name = "User", description = "사용자 관리 API")
public interface UserController {

    @Operation(
        summary = "회원가입",
        description = "새로운 사용자 계정을 생성합니다.",
        responses = {
            @ApiResponse(responseCode = "201", description = "회원가입 성공"),
            @ApiResponse(responseCode = "400", description = "잘못된 요청"),
            @ApiResponse(responseCode = "409", description = "중복된 사용자명 또는 이메일")
        }
    )
    @PostMapping("/api/users/signup")
    ResponseEntity<UserResponse> signup(@RequestBody SignupRequest request);

    @Operation(
        summary = "로그인",
        description = "사용자 계정으로 로그인하여 액세스 토큰을 발급받습니다.",
        responses = {
            @ApiResponse(responseCode = "200", description = "로그인 성공"),
            @ApiResponse(responseCode = "401", description = "인증 실패")
        }
    )
    @PostMapping("/api/users/login")
    ResponseEntity<LoginResponse> login(@RequestBody LoginRequest request);

    @Operation(
        summary = "현재 사용자 정보 조회",
        description = "현재 로그인한 사용자의 정보를 조회합니다.",
        responses = {
            @ApiResponse(responseCode = "200", description = "사용자 정보 조회 성공"),
            @ApiResponse(responseCode = "401", description = "인증 실패")
        }
    )
    @GetMapping("/api/users/me")
    ResponseEntity<UserResponse> getCurrentUser();

    @Operation(
        summary = "프로필 수정",
        description = "현재 사용자의 프로필 정보를 수정합니다.",
        responses = {
            @ApiResponse(responseCode = "200", description = "프로필 수정 성공"),
            @ApiResponse(responseCode = "400", description = "잘못된 요청"),
            @ApiResponse(responseCode = "401", description = "인증 실패"),
            @ApiResponse(responseCode = "409", description = "중복된 사용자명 또는 이메일")
        }
    )
    @PutMapping("/api/users/me")
    ResponseEntity<UserResponse> updateProfile(@RequestBody UpdateProfileRequest request);
}
