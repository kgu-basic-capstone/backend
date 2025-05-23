package uk.jinhy.server.service.user.application;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import uk.jinhy.server.api.user.application.UserService;
import uk.jinhy.server.api.user.domain.User;
import uk.jinhy.server.service.user.domain.UserEntity;
import uk.jinhy.server.service.user.domain.UserRepository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest
@Transactional
public class UserServiceImplTest {

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;

    @DisplayName("로그인한 사용자ID로 사용자 정보를 조회한다.")
    @Test
    void findAuthUser() {
        //given
        String username = "testUsername";

        UserEntity userEntity = UserEntity.builder()
            .oauth2UserId("testId")
            .username(username)
            .build();

        Long userId = userRepository.save(userEntity)
            .getId();

        //when
        User result = userService.getAuthUserInfo("testId");

        //then
        assertThat(result.getId()).isEqualTo(userId);
        assertThat(result.getUsername()).isEqualTo(username);
    }

    @DisplayName("로그인한 사용자ID로 사용자 정보를 조회한다.")
    @Test
    void notExistUserId() {
        //given
        String oauth2UserId = "testUsername";

        //when & then
        assertThatThrownBy(() -> userService.getAuthUserInfo(oauth2UserId))
            .isInstanceOf(IllegalArgumentException.class);
    }

}
