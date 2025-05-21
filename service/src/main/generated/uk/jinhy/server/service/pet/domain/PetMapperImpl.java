package uk.jinhy.server.service.pet.domain;

import java.util.ArrayList;
import java.util.List;
import javax.annotation.processing.Generated;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import uk.jinhy.server.api.pet.domain.HealthRecord;
import uk.jinhy.server.api.pet.domain.Pet;
import uk.jinhy.server.service.user.domain.UserMapper;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-05-20T13:13:32+0000",
    comments = "version: 1.6.3, compiler: IncrementalProcessingEnvironment from gradle-language-java-8.5.jar, environment: Java 17.0.14 (Microsoft)"
)
@Component
public class PetMapperImpl implements PetMapper {

    @Autowired
    private UserMapper userMapper;

    @Override
    public PetEntity toEntity(Pet pet) {
        if ( pet == null ) {
            return null;
        }

        PetEntity.PetEntityBuilder petEntity = PetEntity.builder();

        petEntity.id( pet.getId() );
        petEntity.owner( userMapper.toEntity( pet.getOwner() ) );
        petEntity.name( pet.getName() );
        petEntity.birthDate( pet.getBirthDate() );
        petEntity.healthRecords( healthRecordListToHealthRecordEntityList( pet.getHealthRecords() ) );

        return petEntity.build();
    }

    @Override
    public Pet toDomain(PetEntity petEntity) {
        if ( petEntity == null ) {
            return null;
        }

        Pet.PetBuilder pet = Pet.builder();

        pet.owner( userMapper.toDomain( petEntity.getOwner() ) );
        pet.name( petEntity.getName() );
        pet.birthDate( petEntity.getBirthDate() );

        return pet.build();
    }

    protected HealthRecordEntity healthRecordToHealthRecordEntity(HealthRecord healthRecord) {
        if ( healthRecord == null ) {
            return null;
        }

        HealthRecordEntity.HealthRecordEntityBuilder healthRecordEntity = HealthRecordEntity.builder();

        healthRecordEntity.pet( toEntity( healthRecord.getPet() ) );
        healthRecordEntity.checkDate( healthRecord.getCheckDate() );
        healthRecordEntity.weight( healthRecord.getWeight() );
        healthRecordEntity.category( healthRecord.getCategory() );
        healthRecordEntity.notes( healthRecord.getNotes() );

        return healthRecordEntity.build();
    }

    protected List<HealthRecordEntity> healthRecordListToHealthRecordEntityList(List<HealthRecord> list) {
        if ( list == null ) {
            return null;
        }

        List<HealthRecordEntity> list1 = new ArrayList<HealthRecordEntity>( list.size() );
        for ( HealthRecord healthRecord : list ) {
            list1.add( healthRecordToHealthRecordEntity( healthRecord ) );
        }

        return list1;
    }
}
