package uk.jinhy.server.service.community.domain;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface CommunityCommentRepository extends JpaRepository<CommunityCommentEntity, Long> {
    @Query("SELECT c FROM CommunityCommentEntity c JOIN FETCH c.author WHERE c.id = :id")
    Optional<CommunityCommentEntity> findByIdWithAuthor(@Param("id") Long id);
}
