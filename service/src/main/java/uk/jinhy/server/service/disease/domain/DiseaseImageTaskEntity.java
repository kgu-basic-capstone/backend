package uk.jinhy.server.service.disease.domain;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.TimeToLive;
import uk.jinhy.server.api.disease.domain.DiseaseStatus;

import java.time.LocalDateTime;
import java.util.concurrent.TimeUnit;

@Slf4j
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@RedisHash("disease_image_tasks")
public class DiseaseImageTaskEntity {
    @Id
    private String taskId;
    private String petId;
    private DiseaseStatus status;
    private LocalDateTime requestTime;
    private LocalDateTime resultTime;
    private String base64Image;

    private String resultsJson;

    @Transient
    private JsonNode results;

    private String errorMessage;

    @Builder.Default
    @TimeToLive(unit = TimeUnit.MINUTES)
    private Long ttl = 10L;

    @Transient
    private transient ObjectMapper objectMapper;

    public void setResults(JsonNode results) {
        this.results = results;

        if (results != null) {
            try {
                if (objectMapper == null) {
                    objectMapper = new ObjectMapper();
                }
                this.resultsJson = objectMapper.writeValueAsString(results);
            } catch (JsonProcessingException e) {
                log.error("JsonNode를 String으로 변환 중 오류 발생: {}", e.getMessage(), e);
                this.resultsJson = null;
            }
        } else {
            this.resultsJson = null;
        }
    }

    public JsonNode getResults() {
        if (this.results == null && this.resultsJson != null && !this.resultsJson.isEmpty()) {
            try {
                if (objectMapper == null) {
                    objectMapper = new ObjectMapper();
                }
                this.results = objectMapper.readTree(this.resultsJson);
            } catch (JsonProcessingException e) {
                log.error("String을 JsonNode로 변환 중 오류 발생: {}", e.getMessage(), e);
            }
        }
        return this.results;
    }
}
