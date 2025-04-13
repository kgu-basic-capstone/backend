package uk.jinhy.server.service.domain;


import uk.jinhy.server.service.user.domain.UserEntity;

public class UserFactory {
    private String username;
    private String email;

    private UserFactory() {
        this.username = "defaultUser";
        this.email = "default@example.com";
    }

    public static UserFactory create() {
        return new UserFactory();
    }

    public UserFactory username(String username) {
        this.username = username;
        return this;
    }

    public UserFactory email(String email) {
        this.email = email;
        return this;
    }

    public UserEntity build() {
        return UserEntity.builder()
            .username(this.username)
            .email(this.email)
            .build();
    }
}
