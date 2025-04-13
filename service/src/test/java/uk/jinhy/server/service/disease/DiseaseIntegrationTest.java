package uk.jinhy.server.service.disease;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.transaction.annotation.Transactional;
import uk.jinhy.server.api.disease.domain.DiseaseStatus;
import uk.jinhy.server.service.common.IntegrationTest;
import uk.jinhy.server.service.disease.domain.DiseaseImageRedisRepository;
import uk.jinhy.server.service.disease.domain.DiseaseImageTaskEntity;

import java.time.LocalDateTime;
import java.util.Base64;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@AutoConfigureMockMvc
@Transactional
class DiseaseIntegrationTest extends IntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private DiseaseImageRedisRepository redisRepository;

    @Nested
    @DisplayName("질병 이미지 분석 요청 테스트")
    class AnalyzeImageTest {

        @Test
        @DisplayName("이미지 분석을 요청하면 태스크 ID를 반환한다")
        void 이미지_분석_요청() throws Exception {
            MockMultipartFile imageFile = new MockMultipartFile(
                "image",
                "test-image.jpg",
                MediaType.IMAGE_JPEG_VALUE,
                "테스트 이미지 데이터".getBytes()
            );

            ResultActions result = mockMvc.perform(multipart("/api/diseases")
                .file(imageFile)
                .contentType(MediaType.MULTIPART_FORM_DATA));

            result.andExpect(status().isOk())
                .andExpect(jsonPath("$.taskId").exists())
                .andExpect(jsonPath("$.status").value("PENDING"))
                .andExpect(jsonPath("$.message").exists());
        }
    }

    @Nested
    @DisplayName("질병 이미지 분석 결과 조회 테스트")
    class GetImageAnalysisResultTest {

        private String taskId;

        @BeforeEach
        void setUp() {
            taskId = "test-task-id";
            String base64Image = Base64.getEncoder().encodeToString("테스트 이미지".getBytes());

            String jsonResults = """
                {
                  "predictions": [
                    {
                      "diseaseId": "disease-101",
                      "diseaseName": "Canine Parvovirus",
                      "probability": 0.85
                    },
                    {
                      "diseaseId": "disease-102",
                      "diseaseName": "Canine Distemper",
                      "probability": 0.15
                    }
                  ],
                  "success": true,
                  "processingTime": 850
                }
                """;

            DiseaseImageTaskEntity entity = DiseaseImageTaskEntity.builder()
                .taskId(taskId)
                .status(DiseaseStatus.COMPLETED)
                .requestTime(LocalDateTime.now().minusMinutes(1))
                .resultTime(LocalDateTime.now())
                .base64Image(base64Image)
                .build();

            try {
                JsonNode results = objectMapper.readTree(jsonResults);
                entity.setObjectMapper(objectMapper);
                entity.setResults(results);
            } catch (Exception e) {
                throw new RuntimeException("테스트 설정 중 오류 발생", e);
            }

            redisRepository.save(entity);
        }

        @Test
        @DisplayName("분석 결과를 조회할 수 있다")
        void 분석_결과_조회() throws Exception {
            ResultActions result = mockMvc.perform(get("/api/diseases/{taskId}", taskId)
                .contentType(MediaType.APPLICATION_JSON));

            result.andExpect(status().isOk())
                .andExpect(jsonPath("$.taskId").value(taskId))
                .andExpect(jsonPath("$.status").value("COMPLETED"))
                .andExpect(jsonPath("$.requestTime").exists())
                .andExpect(jsonPath("$.resultTime").exists())
                .andExpect(jsonPath("$.results").exists())
                .andExpect(jsonPath("$.results.predictions").isArray())
                .andExpect(jsonPath("$.results.predictions[0].diseaseName").value("Canine Parvovirus"))
                .andExpect(jsonPath("$.results.success").value(true));
        }

        @Test
        @DisplayName("존재하지 않는 태스크 ID로 조회 시 오류를 반환한다")
        void 존재하지_않는_태스크_ID_조회() throws Exception {
            ResultActions result = mockMvc.perform(get("/api/diseases/non-existent-id")
                .contentType(MediaType.APPLICATION_JSON));

            result.andExpect(status().isNotFound());
        }
    }
}
