package uk.jinhy.server.service.user.presentation;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import uk.jinhy.server.api.user.presentation.UserController;
import uk.jinhy.server.api.user.presentation.UserDto.LoginRequest;
import uk.jinhy.server.api.user.presentation.UserDto.LoginResponse;
import uk.jinhy.server.api.user.presentation.UserDto.SignupRequest;
import uk.jinhy.server.api.user.presentation.UserDto.UpdateProfileRequest;
import uk.jinhy.server.api.user.presentation.UserDto.UserResponse;

@RestController
@RequiredArgsConstructor
public class UserControllerImpl implements UserController {

    @Override
    public ResponseEntity<UserResponse> signup(SignupRequest request) {
        UserResponse mockResponse = UserResponse.builder()
            .id(1L)
            .username(request.getUsername())
            .email(request.getEmail())
            .build();

        return ResponseEntity.status(HttpStatus.CREATED).body(mockResponse);
    }

    @Override
    public ResponseEntity<LoginResponse> login(LoginRequest request) {
        UserResponse mockUser = UserResponse.builder()
            .id(1L)
            .username(request.getUsername())
            .email("user@example.com")
            .build();

        LoginResponse mockResponse = LoginResponse.builder()
            .accessToken("eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiIxIiwibmFtZSI6InRlc3RVc2VyIiwiaWF0IjoxNTE2MjM5MDIyfQ.SflKxwRJSMeKKF2QT4fwpMeJf36POk6yJV_adQssw5c")
            .tokenType("Bearer")
            .user(mockUser)
            .build();

        return ResponseEntity.ok(mockResponse);
    }

    @Override
    public ResponseEntity<UserResponse> getCurrentUser() {
        UserResponse mockResponse = UserResponse.builder()
            .id(1L)
            .username("testUser")
            .email("user@example.com")
            .build();

        return ResponseEntity.ok(mockResponse);
    }

    @Override
    public ResponseEntity<UserResponse> updateProfile(UpdateProfileRequest request) {
        UserResponse mockResponse = UserResponse.builder()
            .id(1L)
            .username(request.getUsername())
            .email(request.getEmail())
            .build();

        return ResponseEntity.ok(mockResponse);
    }
}
