package uk.jinhy.server.service.user.domain;

import org.springframework.data.jpa.repository.JpaRepository;
import uk.jinhy.server.service.domain.UserEntity;

import java.util.Optional;

public interface UserRepository extends JpaRepository<UserEntity, Long> {
    Optional<UserEntity> findByUsername(String username);
    boolean existsByUsername(String username);
    boolean existsByEmail(String email);
}
