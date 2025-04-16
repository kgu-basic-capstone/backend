package uk.jinhy.server.service.vaccination.presentation;

import org.mapstruct.Mapper;
import uk.jinhy.server.service.domain.VaccinationEntity;
import uk.jinhy.server.api.vaccination.presentation.VaccinationDto.VaccinationResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface VaccinationMapper {
    VaccinationMapper INSTANCE = Mappers.getMapper(VaccinationMapper.class);

    @Mapping(source = "pet.id", target = "petId")  // Entity 내 pet 객체의 id를 DTO의 petId로 매핑
    VaccinationResponse fromEntity(VaccinationEntity entity);

}
