package uk.jinhy.server.service.user.presentation;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.RestController;
import uk.jinhy.server.api.user.application.UserService;
import uk.jinhy.server.api.user.domain.User;
import uk.jinhy.server.api.user.presentation.UserController;
import uk.jinhy.server.api.user.presentation.dto.response.AuthenticatedUserInfoResponseDto;
import uk.jinhy.server.service.user.domain.UserMapper;

@RestController
@RequiredArgsConstructor
public class UserControllerImpl implements UserController {

    private final UserService userService;
    private final UserMapper userMapper;

    @Override
    public ResponseEntity<AuthenticatedUserInfoResponseDto> getAuthUserInfo(
        @AuthenticationPrincipal String oauth2UserId
    ) {
        User user = userService.getAuthUserInfo(oauth2UserId);
        AuthenticatedUserInfoResponseDto response = userMapper.toAuthenticatedUserInfoResponseDto(user);
        return ResponseEntity.ok(response);
    }
}
