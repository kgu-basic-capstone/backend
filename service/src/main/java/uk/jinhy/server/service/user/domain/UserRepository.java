package uk.jinhy.server.service.user.domain;

import org.springframework.data.jpa.repository.JpaRepository;
import uk.jinhy.server.service.domain.UserEntity;

import java.util.Optional;

 //임시로 만든 UserRepository
public interface UserRepository extends JpaRepository<UserEntity, Long> {
    Optional<UserEntity> findById(Long id);
}
