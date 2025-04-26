package uk.jinhy.server.api.disease.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import com.fasterxml.jackson.databind.JsonNode;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DiseaseImageTask {
    private String taskId;
    private DiseaseStatus status;
    private LocalDateTime requestTime;
    private LocalDateTime resultTime;
    private JsonNode results;
    private String errorMessage;
    private String message;
}
