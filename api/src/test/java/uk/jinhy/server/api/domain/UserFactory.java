    package uk.jinhy.server.api.domain;

    import uk.jinhy.server.api.user.domain.User;

    public class UserFactory {

        public static User.UserBuilder create() {
            return User.builder()
                .username("testUser")
                .email("test@test.com");
        }

    }
