package uk.jinhy.server.service.user.presentation;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import uk.jinhy.server.api.user.application.UserService;
import uk.jinhy.server.api.user.domain.User;
import uk.jinhy.server.api.user.presentation.UserController;
import uk.jinhy.server.api.user.presentation.dto.response.AuthenticatedUserInfoResponseDto;
import uk.jinhy.server.service.user.domain.UserMapper;
import uk.jinhy.server.api.user.application.CurrentUser;

@RestController
@RequiredArgsConstructor
public class UserControllerImpl implements UserController {
    private final UserMapper userMapper;

    public ResponseEntity<AuthenticatedUserInfoResponseDto> getAuthUserInfo(
        @CurrentUser User user
    ) {
        AuthenticatedUserInfoResponseDto response = userMapper.toAuthenticatedUserInfoResponseDto(user);
        return ResponseEntity.ok(response);
    }
}
