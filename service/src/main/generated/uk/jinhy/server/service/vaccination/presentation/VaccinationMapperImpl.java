package uk.jinhy.server.service.vaccination.presentation;

import javax.annotation.processing.Generated;
import uk.jinhy.server.api.vaccination.presentation.VaccinationDto;
import uk.jinhy.server.service.domain.VaccinationEntity;
import uk.jinhy.server.service.pet.domain.PetEntity;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-05-20T13:13:32+0000",
    comments = "version: 1.6.3, compiler: IncrementalProcessingEnvironment from gradle-language-java-8.5.jar, environment: Java 17.0.14 (Microsoft)"
)
public class VaccinationMapperImpl implements VaccinationMapper {

    @Override
    public VaccinationDto.VaccinationResponse fromEntity(VaccinationEntity entity) {
        if ( entity == null ) {
            return null;
        }

        VaccinationDto.VaccinationResponse.VaccinationResponseBuilder vaccinationResponse = VaccinationDto.VaccinationResponse.builder();

        vaccinationResponse.petId( entityPetId( entity ) );
        vaccinationResponse.id( entity.getId() );
        vaccinationResponse.vaccineName( entity.getVaccineName() );
        vaccinationResponse.vaccinationDate( entity.getVaccinationDate() );
        vaccinationResponse.nextVaccinationDate( entity.getNextVaccinationDate() );

        return vaccinationResponse.build();
    }

    private Long entityPetId(VaccinationEntity vaccinationEntity) {
        PetEntity pet = vaccinationEntity.getPet();
        if ( pet == null ) {
            return null;
        }
        return pet.getId();
    }
}
