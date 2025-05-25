package uk.jinhy.server.service.domain;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.dsl.StringTemplate;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QHospitalReservationEntity is a Querydsl query type for HospitalReservationEntity
 */
@SuppressWarnings("this-escape")
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QHospitalReservationEntity extends EntityPathBase<HospitalReservationEntity> {

    private static final long serialVersionUID = -1563019671L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QHospitalReservationEntity hospitalReservationEntity = new QHospitalReservationEntity("hospitalReservationEntity");

    public final QHospitalEntity hospital;

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final uk.jinhy.server.service.pet.domain.QPetEntity pet;

    public final DateTimePath<java.time.LocalDateTime> reservationDateTime = createDateTime("reservationDateTime", java.time.LocalDateTime.class);

    public final EnumPath<HospitalReservationEntity.ReservationStatus> status = createEnum("status", HospitalReservationEntity.ReservationStatus.class);

    public final uk.jinhy.server.service.user.domain.QUserEntity user;

    public QHospitalReservationEntity(String variable) {
        this(HospitalReservationEntity.class, forVariable(variable), INITS);
    }

    public QHospitalReservationEntity(Path<? extends HospitalReservationEntity> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QHospitalReservationEntity(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QHospitalReservationEntity(PathMetadata metadata, PathInits inits) {
        this(HospitalReservationEntity.class, metadata, inits);
    }

    public QHospitalReservationEntity(Class<? extends HospitalReservationEntity> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.hospital = inits.isInitialized("hospital") ? new QHospitalEntity(forProperty("hospital")) : null;
        this.pet = inits.isInitialized("pet") ? new uk.jinhy.server.service.pet.domain.QPetEntity(forProperty("pet"), inits.get("pet")) : null;
        this.user = inits.isInitialized("user") ? new uk.jinhy.server.service.user.domain.QUserEntity(forProperty("user")) : null;
    }

}

