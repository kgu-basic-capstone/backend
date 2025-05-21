package uk.jinhy.server.service.user.application;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import uk.jinhy.server.api.user.application.UserService;
import uk.jinhy.server.api.user.domain.User;
import uk.jinhy.server.service.user.domain.UserEntity;
import uk.jinhy.server.service.user.domain.UserMapper;
import uk.jinhy.server.service.user.domain.UserRepository;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;

    @Override
    public User getAuthUserInfo(String oauth2UserId) {
        UserEntity userEntity = userRepository.findByOauth2UserId(oauth2UserId)
            .orElseThrow(IllegalArgumentException::new);

        return userMapper.toDomain(userEntity);
    }

}
