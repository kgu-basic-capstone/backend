package uk.jinhy.server.service.domain;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.dsl.StringTemplate;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QHospitalEntity is a Querydsl query type for HospitalEntity
 */
@SuppressWarnings("this-escape")
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QHospitalEntity extends EntityPathBase<HospitalEntity> {

    private static final long serialVersionUID = -1776290679L;

    public static final QHospitalEntity hospitalEntity = new QHospitalEntity("hospitalEntity");

    public final StringPath address = createString("address");

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final NumberPath<Double> latitude = createNumber("latitude", Double.class);

    public final NumberPath<Double> longitude = createNumber("longitude", Double.class);

    public final StringPath name = createString("name");

    public final StringPath operatingHours = createString("operatingHours");

    public final StringPath phoneNumber = createString("phoneNumber");

    public final NumberPath<Double> rating = createNumber("rating", Double.class);

    public final ListPath<HospitalReservationEntity, QHospitalReservationEntity> reservations = this.<HospitalReservationEntity, QHospitalReservationEntity>createList("reservations", HospitalReservationEntity.class, QHospitalReservationEntity.class, PathInits.DIRECT2);

    public final BooleanPath surgeryAvailable = createBoolean("surgeryAvailable");

    public QHospitalEntity(String variable) {
        super(HospitalEntity.class, forVariable(variable));
    }

    public QHospitalEntity(Path<? extends HospitalEntity> path) {
        super(path.getType(), path.getMetadata());
    }

    public QHospitalEntity(PathMetadata metadata) {
        super(HospitalEntity.class, metadata);
    }

}

