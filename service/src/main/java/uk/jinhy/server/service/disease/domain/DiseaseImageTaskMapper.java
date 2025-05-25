package uk.jinhy.server.service.disease.domain;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;
import uk.jinhy.server.api.disease.domain.DiseaseImageTask;
import uk.jinhy.server.api.disease.presentation.dto.response.DiseaseImageResponseDto;
import uk.jinhy.server.api.disease.presentation.dto.response.DiseaseImageResultResponseDto;

@Mapper(componentModel = "spring")
public interface DiseaseImageTaskMapper {
    DiseaseImageTaskMapper INSTANCE = Mappers.getMapper(DiseaseImageTaskMapper.class);

    DiseaseImageTask entityToDomain(DiseaseImageTaskEntity entity);

    @Mapping(target = "ttl", ignore = true)
    @Mapping(target = "resultsJson", ignore = true)
    DiseaseImageTaskEntity domainToEntity(DiseaseImageTask domain);

    DiseaseImageResponseDto domainToResponseDto(DiseaseImageTask domain);

    DiseaseImageResultResponseDto domainToResultResponseDto(DiseaseImageTask domain);
}
