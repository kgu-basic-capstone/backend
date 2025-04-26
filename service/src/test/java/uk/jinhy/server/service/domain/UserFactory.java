package uk.jinhy.server.service.domain;


import uk.jinhy.server.service.user.domain.UserEntity;

public class UserFactory {
    private String username;

    private UserFactory() {
        this.username = "defaultUser";
    }

    public static UserFactory create() {
        return new UserFactory();
    }

    public UserFactory username(String username) {
        this.username = username;
        return this;
    }

    public UserEntity build() {
        return UserEntity.builder()
            .username(this.username)
            .build();
    }
}
