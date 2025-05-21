package uk.jinhy.server.service.domain;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.dsl.StringTemplate;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QVaccinationEntity is a Querydsl query type for VaccinationEntity
 */
@SuppressWarnings("this-escape")
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QVaccinationEntity extends EntityPathBase<VaccinationEntity> {

    private static final long serialVersionUID = 1752781116L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QVaccinationEntity vaccinationEntity = new QVaccinationEntity("vaccinationEntity");

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final BooleanPath isCompleted = createBoolean("isCompleted");

    public final DatePath<java.time.LocalDate> nextVaccinationDate = createDate("nextVaccinationDate", java.time.LocalDate.class);

    public final uk.jinhy.server.service.pet.domain.QPetEntity pet;

    public final DatePath<java.time.LocalDate> vaccinationDate = createDate("vaccinationDate", java.time.LocalDate.class);

    public final StringPath vaccineName = createString("vaccineName");

    public QVaccinationEntity(String variable) {
        this(VaccinationEntity.class, forVariable(variable), INITS);
    }

    public QVaccinationEntity(Path<? extends VaccinationEntity> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QVaccinationEntity(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QVaccinationEntity(PathMetadata metadata, PathInits inits) {
        this(VaccinationEntity.class, metadata, inits);
    }

    public QVaccinationEntity(Class<? extends VaccinationEntity> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.pet = inits.isInitialized("pet") ? new uk.jinhy.server.service.pet.domain.QPetEntity(forProperty("pet"), inits.get("pet")) : null;
    }

}

