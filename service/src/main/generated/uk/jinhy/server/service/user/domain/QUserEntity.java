package uk.jinhy.server.service.user.domain;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.dsl.StringTemplate;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QUserEntity is a Querydsl query type for UserEntity
 */
@SuppressWarnings("this-escape")
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QUserEntity extends EntityPathBase<UserEntity> {

    private static final long serialVersionUID = -2071942989L;

    public static final QUserEntity userEntity = new QUserEntity("userEntity");

    public final StringPath email = createString("email");

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final StringPath oauth2UserId = createString("oauth2UserId");

    public final ListPath<uk.jinhy.server.service.pet.domain.PetEntity, uk.jinhy.server.service.pet.domain.QPetEntity> pets = this.<uk.jinhy.server.service.pet.domain.PetEntity, uk.jinhy.server.service.pet.domain.QPetEntity>createList("pets", uk.jinhy.server.service.pet.domain.PetEntity.class, uk.jinhy.server.service.pet.domain.QPetEntity.class, PathInits.DIRECT2);

    public final StringPath username = createString("username");

    public QUserEntity(String variable) {
        super(UserEntity.class, forVariable(variable));
    }

    public QUserEntity(Path<? extends UserEntity> path) {
        super(path.getType(), path.getMetadata());
    }

    public QUserEntity(PathMetadata metadata) {
        super(UserEntity.class, metadata);
    }

}

