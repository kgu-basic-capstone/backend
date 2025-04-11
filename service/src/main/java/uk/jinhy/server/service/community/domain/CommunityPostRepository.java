package uk.jinhy.server.service.community.domain;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import uk.jinhy.server.api.community.domain.Category;

import java.util.Optional;

public interface CommunityPostRepository extends JpaRepository<CommunityPostEntity, Long>, CommunityPostRepositoryCustom {
    @Query("SELECT p FROM CommunityPostEntity p WHERE (:category IS NULL OR p.category = :category) " +
        "AND (:keyword IS NULL OR p.title LIKE %:keyword% OR p.content LIKE %:keyword%)")
    Page<CommunityPostEntity> findBySearchConditions(
        @Param("category") Category category,
        @Param("keyword") String keyword,
        Pageable pageable);

    @Query("SELECT p FROM CommunityPostEntity p JOIN FETCH p.author WHERE p.id = :id")
    Optional<CommunityPostEntity> findByIdWithAuthor(@Param("id") Long id);

    @Query("SELECT p FROM CommunityPostEntity p JOIN FETCH p.author LEFT JOIN FETCH p.comments c LEFT JOIN FETCH c.author WHERE p.id = :id")
    Optional<CommunityPostEntity> findByIdWithAuthorAndComments(@Param("id") Long id);
}
