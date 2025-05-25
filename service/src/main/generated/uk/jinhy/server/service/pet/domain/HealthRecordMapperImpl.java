package uk.jinhy.server.service.pet.domain;

import javax.annotation.processing.Generated;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import uk.jinhy.server.api.pet.domain.HealthRecord;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-05-20T13:13:32+0000",
    comments = "version: 1.6.3, compiler: IncrementalProcessingEnvironment from gradle-language-java-8.5.jar, environment: Java 17.0.14 (Microsoft)"
)
@Component
public class HealthRecordMapperImpl implements HealthRecordMapper {

    @Autowired
    private PetMapper petMapper;

    @Override
    public HealthRecordEntity toEntity(HealthRecord domain) {
        if ( domain == null ) {
            return null;
        }

        HealthRecordEntity.HealthRecordEntityBuilder healthRecordEntity = HealthRecordEntity.builder();

        healthRecordEntity.pet( petMapper.toEntity( domain.getPet() ) );
        healthRecordEntity.checkDate( domain.getCheckDate() );
        healthRecordEntity.weight( domain.getWeight() );
        healthRecordEntity.category( domain.getCategory() );
        healthRecordEntity.notes( domain.getNotes() );

        return healthRecordEntity.build();
    }

    @Override
    public HealthRecord toDomain(HealthRecordEntity entity) {
        if ( entity == null ) {
            return null;
        }

        HealthRecord.HealthRecordBuilder healthRecord = HealthRecord.builder();

        healthRecord.pet( petMapper.toDomain( entity.getPet() ) );
        healthRecord.checkDate( entity.getCheckDate() );
        healthRecord.weight( entity.getWeight() );
        healthRecord.category( entity.getCategory() );
        healthRecord.notes( entity.getNotes() );

        return healthRecord.build();
    }
}
