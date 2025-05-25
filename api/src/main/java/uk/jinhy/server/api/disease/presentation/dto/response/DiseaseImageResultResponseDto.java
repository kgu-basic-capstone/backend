package uk.jinhy.server.api.disease.presentation.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import uk.jinhy.server.api.disease.domain.DiseaseStatus;
import com.fasterxml.jackson.databind.JsonNode;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DiseaseImageResultResponseDto {
    private String taskId;
    private DiseaseStatus status;
    private LocalDateTime requestTime;
    private LocalDateTime resultTime;
    private JsonNode results;
    private String errorMessage;
} 