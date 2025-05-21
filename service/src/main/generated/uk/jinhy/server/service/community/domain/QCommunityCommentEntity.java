package uk.jinhy.server.service.community.domain;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.dsl.StringTemplate;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QCommunityCommentEntity is a Querydsl query type for CommunityCommentEntity
 */
@SuppressWarnings("this-escape")
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QCommunityCommentEntity extends EntityPathBase<CommunityCommentEntity> {

    private static final long serialVersionUID = 570468800L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QCommunityCommentEntity communityCommentEntity = new QCommunityCommentEntity("communityCommentEntity");

    public final uk.jinhy.server.service.user.domain.QUserEntity author;

    public final StringPath content = createString("content");

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final QCommunityPostEntity post;

    public QCommunityCommentEntity(String variable) {
        this(CommunityCommentEntity.class, forVariable(variable), INITS);
    }

    public QCommunityCommentEntity(Path<? extends CommunityCommentEntity> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QCommunityCommentEntity(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QCommunityCommentEntity(PathMetadata metadata, PathInits inits) {
        this(CommunityCommentEntity.class, metadata, inits);
    }

    public QCommunityCommentEntity(Class<? extends CommunityCommentEntity> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.author = inits.isInitialized("author") ? new uk.jinhy.server.service.user.domain.QUserEntity(forProperty("author")) : null;
        this.post = inits.isInitialized("post") ? new QCommunityPostEntity(forProperty("post"), inits.get("post")) : null;
    }

}

