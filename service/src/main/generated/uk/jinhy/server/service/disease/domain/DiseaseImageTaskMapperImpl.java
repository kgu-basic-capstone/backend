package uk.jinhy.server.service.disease.domain;

import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;
import uk.jinhy.server.api.disease.domain.DiseaseImageTask;
import uk.jinhy.server.api.disease.presentation.dto.response.DiseaseImageResponseDto;
import uk.jinhy.server.api.disease.presentation.dto.response.DiseaseImageResultResponseDto;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-05-20T13:13:32+0000",
    comments = "version: 1.6.3, compiler: IncrementalProcessingEnvironment from gradle-language-java-8.5.jar, environment: Java 17.0.14 (Microsoft)"
)
@Component
public class DiseaseImageTaskMapperImpl implements DiseaseImageTaskMapper {

    @Override
    public DiseaseImageTask entityToDomain(DiseaseImageTaskEntity entity) {
        if ( entity == null ) {
            return null;
        }

        DiseaseImageTask.DiseaseImageTaskBuilder diseaseImageTask = DiseaseImageTask.builder();

        diseaseImageTask.taskId( entity.getTaskId() );
        diseaseImageTask.status( entity.getStatus() );
        diseaseImageTask.requestTime( entity.getRequestTime() );
        diseaseImageTask.resultTime( entity.getResultTime() );
        diseaseImageTask.results( entity.getResults() );
        diseaseImageTask.errorMessage( entity.getErrorMessage() );

        return diseaseImageTask.build();
    }

    @Override
    public DiseaseImageTaskEntity domainToEntity(DiseaseImageTask domain) {
        if ( domain == null ) {
            return null;
        }

        DiseaseImageTaskEntity.DiseaseImageTaskEntityBuilder diseaseImageTaskEntity = DiseaseImageTaskEntity.builder();

        diseaseImageTaskEntity.taskId( domain.getTaskId() );
        diseaseImageTaskEntity.status( domain.getStatus() );
        diseaseImageTaskEntity.requestTime( domain.getRequestTime() );
        diseaseImageTaskEntity.resultTime( domain.getResultTime() );
        diseaseImageTaskEntity.results( domain.getResults() );
        diseaseImageTaskEntity.errorMessage( domain.getErrorMessage() );

        return diseaseImageTaskEntity.build();
    }

    @Override
    public DiseaseImageResponseDto domainToResponseDto(DiseaseImageTask domain) {
        if ( domain == null ) {
            return null;
        }

        DiseaseImageResponseDto.DiseaseImageResponseDtoBuilder diseaseImageResponseDto = DiseaseImageResponseDto.builder();

        diseaseImageResponseDto.taskId( domain.getTaskId() );
        diseaseImageResponseDto.status( domain.getStatus() );
        diseaseImageResponseDto.message( domain.getMessage() );

        return diseaseImageResponseDto.build();
    }

    @Override
    public DiseaseImageResultResponseDto domainToResultResponseDto(DiseaseImageTask domain) {
        if ( domain == null ) {
            return null;
        }

        DiseaseImageResultResponseDto.DiseaseImageResultResponseDtoBuilder diseaseImageResultResponseDto = DiseaseImageResultResponseDto.builder();

        diseaseImageResultResponseDto.taskId( domain.getTaskId() );
        diseaseImageResultResponseDto.status( domain.getStatus() );
        diseaseImageResultResponseDto.requestTime( domain.getRequestTime() );
        diseaseImageResultResponseDto.resultTime( domain.getResultTime() );
        diseaseImageResultResponseDto.results( domain.getResults() );
        diseaseImageResultResponseDto.errorMessage( domain.getErrorMessage() );

        return diseaseImageResultResponseDto.build();
    }
}
