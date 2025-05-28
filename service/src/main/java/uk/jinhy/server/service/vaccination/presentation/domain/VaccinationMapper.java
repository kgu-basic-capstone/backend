package uk.jinhy.server.service.vaccination.presentation.domain;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;
import uk.jinhy.server.api.vaccination.presentation.VaccinationDto;

@Mapper
public interface VaccinationMapper {
    VaccinationMapper INSTANCE = Mappers.getMapper(VaccinationMapper.class);

    @Mapping(source = "pet.id", target = "petId")
    @Mapping(source = "vaccinationStatus", target = "statusType")
    VaccinationDto.VaccinationResponse fromEntity(VaccinationEntity entity);

}
