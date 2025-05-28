package uk.jinhy.server.service.community.domain;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface CommunityPostRepository extends JpaRepository<CommunityPostEntity, Long>, CommunityPostRepositoryCustom {
    @Query("SELECT p FROM CommunityPostEntity p JOIN FETCH p.author WHERE p.id = :id")
    Optional<CommunityPostEntity> findByIdWithAuthor(@Param("id") Long id);

    @Query("SELECT p FROM CommunityPostEntity p JOIN FETCH p.author LEFT JOIN FETCH p.comments c LEFT JOIN FETCH c.author WHERE p.id = :id")
    Optional<CommunityPostEntity> findByIdWithAuthorAndComments(@Param("id") Long id);
}
