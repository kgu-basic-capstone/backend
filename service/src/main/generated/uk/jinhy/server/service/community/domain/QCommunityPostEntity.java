package uk.jinhy.server.service.community.domain;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.dsl.StringTemplate;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QCommunityPostEntity is a Querydsl query type for CommunityPostEntity
 */
@SuppressWarnings("this-escape")
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QCommunityPostEntity extends EntityPathBase<CommunityPostEntity> {

    private static final long serialVersionUID = -46549307L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QCommunityPostEntity communityPostEntity = new QCommunityPostEntity("communityPostEntity");

    public final uk.jinhy.server.service.user.domain.QUserEntity author;

    public final EnumPath<uk.jinhy.server.api.community.domain.Category> category = createEnum("category", uk.jinhy.server.api.community.domain.Category.class);

    public final ListPath<CommunityCommentEntity, QCommunityCommentEntity> comments = this.<CommunityCommentEntity, QCommunityCommentEntity>createList("comments", CommunityCommentEntity.class, QCommunityCommentEntity.class, PathInits.DIRECT2);

    public final StringPath content = createString("content");

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final StringPath title = createString("title");

    public QCommunityPostEntity(String variable) {
        this(CommunityPostEntity.class, forVariable(variable), INITS);
    }

    public QCommunityPostEntity(Path<? extends CommunityPostEntity> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QCommunityPostEntity(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QCommunityPostEntity(PathMetadata metadata, PathInits inits) {
        this(CommunityPostEntity.class, metadata, inits);
    }

    public QCommunityPostEntity(Class<? extends CommunityPostEntity> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.author = inits.isInitialized("author") ? new uk.jinhy.server.service.user.domain.QUserEntity(forProperty("author")) : null;
    }

}

