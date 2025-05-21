package uk.jinhy.server.service.disease.application;

import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.stereotype.Component;

@Component
public interface AiModelClient {
    JsonNode analyzeImage(String base64Image);
}
