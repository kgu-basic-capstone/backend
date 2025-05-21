package uk.jinhy.server.api.user.application;

import uk.jinhy.server.api.user.domain.User;

public interface UserService {
    User getAuthUserInfo(String oauth2UserId);
}
