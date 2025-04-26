package uk.jinhy.server.service.disease.application;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "type")
public class DiseaseImageMessage {
    @JsonProperty("type")
    private String type = "diseaseImageMessage";

    private String taskId;
    private LocalDateTime requestTime;
}
