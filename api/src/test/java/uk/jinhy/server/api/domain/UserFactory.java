    package uk.jinhy.server.api.domain;

    public class UserFactory {

        public static User.UserBuilder create() {
            return User.builder()
                .username("testUser")
                .email("test@test.com");
        }

    }
