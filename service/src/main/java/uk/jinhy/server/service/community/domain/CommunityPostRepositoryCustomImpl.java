package uk.jinhy.server.service.community.domain;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import uk.jinhy.server.api.community.domain.Category;

import java.util.List;

@RequiredArgsConstructor
@Repository
public class CommunityPostRepositoryCustomImpl implements CommunityPostRepositoryCustom {
    private final JPAQueryFactory queryFactory;

    @Override
    public Page<CommunityPostEntity> findBySearchConditions(Category category, String keyword, Pageable pageable) {
        QCommunityPostEntity post = QCommunityPostEntity.communityPostEntity;

        BooleanExpression predicate = null;
        if (category != null) {
            predicate = post.category.eq(category);
        }

        if (keyword != null && !keyword.isEmpty()) {
            BooleanExpression keywordCondition = post.title.containsIgnoreCase(keyword)
                .or(post.content.containsIgnoreCase(keyword));

            predicate = predicate != null
                ? predicate.and(keywordCondition)
                : keywordCondition;
        }

        long total = queryFactory
            .select(post.count())
            .from(post)
            .where(predicate)
            .fetchOne();

        List<CommunityPostEntity> content = queryFactory
            .selectFrom(post)
            .where(predicate)
            .offset(pageable.getOffset())
            .limit(pageable.getPageSize())
            .fetch();

        return new PageImpl<>(content, pageable, total);
    }
}
