package uk.jinhy.server.service.community.domain;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import uk.jinhy.server.api.community.domain.Category;

public interface CommunityPostRepositoryCustom {
    Page<CommunityPostEntity> findBySearchConditions(Category category, String keyword, Pageable pageable);
}
