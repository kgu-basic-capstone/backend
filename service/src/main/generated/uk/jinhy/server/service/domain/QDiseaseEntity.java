package uk.jinhy.server.service.domain;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.dsl.StringTemplate;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QDiseaseEntity is a Querydsl query type for DiseaseEntity
 */
@SuppressWarnings("this-escape")
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QDiseaseEntity extends EntityPathBase<DiseaseEntity> {

    private static final long serialVersionUID = -319352525L;

    public static final QDiseaseEntity diseaseEntity = new QDiseaseEntity("diseaseEntity");

    public final StringPath description = createString("description");

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final StringPath name = createString("name");

    public final ListPath<String, StringPath> possibleCauses = this.<String, StringPath>createList("possibleCauses", String.class, StringPath.class, PathInits.DIRECT2);

    public final ListPath<String, StringPath> recommendedCare = this.<String, StringPath>createList("recommendedCare", String.class, StringPath.class, PathInits.DIRECT2);

    public final ListPath<String, StringPath> recommendedDiet = this.<String, StringPath>createList("recommendedDiet", String.class, StringPath.class, PathInits.DIRECT2);

    public final ListPath<String, StringPath> recommendedTreatments = this.<String, StringPath>createList("recommendedTreatments", String.class, StringPath.class, PathInits.DIRECT2);

    public QDiseaseEntity(String variable) {
        super(DiseaseEntity.class, forVariable(variable));
    }

    public QDiseaseEntity(Path<? extends DiseaseEntity> path) {
        super(path.getType(), path.getMetadata());
    }

    public QDiseaseEntity(PathMetadata metadata) {
        super(DiseaseEntity.class, metadata);
    }

}

