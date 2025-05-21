package uk.jinhy.server.service.pet.domain;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.dsl.StringTemplate;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QHealthRecordEntity is a Querydsl query type for HealthRecordEntity
 */
@SuppressWarnings("this-escape")
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QHealthRecordEntity extends EntityPathBase<HealthRecordEntity> {

    private static final long serialVersionUID = 605524685L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QHealthRecordEntity healthRecordEntity = new QHealthRecordEntity("healthRecordEntity");

    public final EnumPath<uk.jinhy.server.api.pet.domain.HealthRecordCategories> category = createEnum("category", uk.jinhy.server.api.pet.domain.HealthRecordCategories.class);

    public final DatePath<java.time.LocalDate> checkDate = createDate("checkDate", java.time.LocalDate.class);

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final StringPath notes = createString("notes");

    public final QPetEntity pet;

    public final NumberPath<Double> weight = createNumber("weight", Double.class);

    public QHealthRecordEntity(String variable) {
        this(HealthRecordEntity.class, forVariable(variable), INITS);
    }

    public QHealthRecordEntity(Path<? extends HealthRecordEntity> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QHealthRecordEntity(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QHealthRecordEntity(PathMetadata metadata, PathInits inits) {
        this(HealthRecordEntity.class, metadata, inits);
    }

    public QHealthRecordEntity(Class<? extends HealthRecordEntity> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.pet = inits.isInitialized("pet") ? new QPetEntity(forProperty("pet"), inits.get("pet")) : null;
    }

}

