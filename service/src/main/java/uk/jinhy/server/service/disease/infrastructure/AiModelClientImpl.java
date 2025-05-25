package uk.jinhy.server.service.disease.infrastructure;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import uk.jinhy.server.service.disease.application.AiModelClient;

import java.util.Random;

@Component
@RequiredArgsConstructor
public class AiModelClientImpl implements AiModelClient {
    private final ObjectMapper objectMapper;
    private final Random random = new Random();

    @Override
    public JsonNode analyzeImage(String base64Image) {
        try {
            Thread.sleep(10000 + random.nextInt(5000));

            String jsonResponse = """
                {
                  "predictions": [
                    {
                      "diseaseId": "disease-101",
                      "diseaseName": "Canine Parvovirus",
                      "probability": %f
                    },
                    {
                      "diseaseId": "disease-102",
                      "diseaseName": "Canine Distemper",
                      "probability": %f
                    },
                    {
                      "diseaseId": "disease-103",
                      "diseaseName": "Kennel Cough",
                      "probability": %f
                    }
                  ],
                  "success": true,
                  "processingTime": %d
                }
                """.formatted(
                    random.nextDouble() * 0.5 + 0.5,
                    random.nextDouble() * 0.3 + 0.1,
                    random.nextDouble() * 0.2 + 0.1,
                    random.nextInt(1000) + 500
                );

            return objectMapper.readTree(jsonResponse);
        } catch (Exception e) {
            throw new RuntimeException("Failed to analyze image", e);
        }
    }
}
