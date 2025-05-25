package uk.jinhy.server.service.user.presentation;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import uk.jinhy.server.api.user.application.UserService;
import uk.jinhy.server.api.user.domain.User;
import uk.jinhy.server.service.user.domain.UserEntity;
import uk.jinhy.server.service.user.domain.UserRepository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class UserControllerImplTest {

//    @Autowired
//    private WebApplicationContext context;

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private UserService userService;

    @MockitoBean
    private UserRepository userRepository;

    @DisplayName("로그인한 회원의 정보를 반환한다.")
    @Test
    void getAuthUserInfo() throws Exception {
        //given
        String oauth2UserId = "testId";
        Authentication authentication = generateAuthentication(oauth2UserId);
        SecurityContextHolder.getContext().setAuthentication(authentication);

        UserEntity userEntity = Mockito.mock(UserEntity.class);

        long userId = 1L;
        String username = "username";
        User user = User.builder()
            .id(userId)
            .username(username)
            .build();

        when(userRepository.findByOauth2UserId(anyString()))
            .thenReturn(Optional.of(userEntity));

        when(userService.getAuthUserInfo(anyString()))
            .thenReturn(user);

        //when & then
        mockMvc.perform(
                get("/api/users/me")
            )
            .andDo(print())
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.id").value(userId))
            .andExpect(jsonPath("$.username").value(username));
    }

    private Authentication generateAuthentication(String oauth2UserId) {
        Collection<GrantedAuthority> authorities = List.of(new SimpleGrantedAuthority("ROLE_USER"));
        return new UsernamePasswordAuthenticationToken(oauth2UserId, null, authorities);
    }
}
