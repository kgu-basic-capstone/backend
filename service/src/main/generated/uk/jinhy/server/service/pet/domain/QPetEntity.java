package uk.jinhy.server.service.pet.domain;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.dsl.StringTemplate;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QPetEntity is a Querydsl query type for PetEntity
 */
@SuppressWarnings("this-escape")
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QPetEntity extends EntityPathBase<PetEntity> {

    private static final long serialVersionUID = -925070715L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QPetEntity petEntity = new QPetEntity("petEntity");

    public final DatePath<java.time.LocalDate> birthDate = createDate("birthDate", java.time.LocalDate.class);

    public final ListPath<HealthRecordEntity, QHealthRecordEntity> healthRecords = this.<HealthRecordEntity, QHealthRecordEntity>createList("healthRecords", HealthRecordEntity.class, QHealthRecordEntity.class, PathInits.DIRECT2);

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final StringPath name = createString("name");

    public final uk.jinhy.server.service.user.domain.QUserEntity owner;

    public final ListPath<uk.jinhy.server.service.domain.VaccinationEntity, uk.jinhy.server.service.domain.QVaccinationEntity> vaccinations = this.<uk.jinhy.server.service.domain.VaccinationEntity, uk.jinhy.server.service.domain.QVaccinationEntity>createList("vaccinations", uk.jinhy.server.service.domain.VaccinationEntity.class, uk.jinhy.server.service.domain.QVaccinationEntity.class, PathInits.DIRECT2);

    public QPetEntity(String variable) {
        this(PetEntity.class, forVariable(variable), INITS);
    }

    public QPetEntity(Path<? extends PetEntity> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QPetEntity(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QPetEntity(PathMetadata metadata, PathInits inits) {
        this(PetEntity.class, metadata, inits);
    }

    public QPetEntity(Class<? extends PetEntity> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.owner = inits.isInitialized("owner") ? new uk.jinhy.server.service.user.domain.QUserEntity(forProperty("owner")) : null;
    }

}

