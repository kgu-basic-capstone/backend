package uk.jinhy.server.api.disease.presentation.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import uk.jinhy.server.api.disease.domain.DiseaseStatus;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DiseaseImageResponseDto {
    private String taskId;
    private DiseaseStatus status;
    private String message;
} 