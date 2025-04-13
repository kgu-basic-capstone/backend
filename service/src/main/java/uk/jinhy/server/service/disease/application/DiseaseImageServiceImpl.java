package uk.jinhy.server.service.disease.application;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import uk.jinhy.server.api.disease.application.DiseaseImageService;
import uk.jinhy.server.api.disease.domain.DiseaseImageTask;
import uk.jinhy.server.api.disease.domain.DiseaseStatus;
import uk.jinhy.server.api.disease.domain.exception.TaskNotFoundException;
import uk.jinhy.server.api.disease.presentation.dto.request.DiseaseImageRequestDto;
import uk.jinhy.server.api.disease.presentation.dto.response.DiseaseImageResponseDto;
import uk.jinhy.server.api.disease.presentation.dto.response.DiseaseImageResultResponseDto;
import uk.jinhy.server.service.disease.domain.DiseaseImageTaskEntity;
import uk.jinhy.server.service.disease.domain.DiseaseImageTaskMapper;
import uk.jinhy.server.service.disease.domain.DiseaseImageRedisRepository;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Base64;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class DiseaseImageServiceImpl implements DiseaseImageService {
    private final DiseaseImageRedisRepository redisRepository;
    private final DiseaseImageProducer producer;
    private final DiseaseImageTaskMapper mapper;

    @Override
    public DiseaseImageResponseDto requestImageAnalysis(DiseaseImageRequestDto requestDto) throws IOException {
        String taskId = UUID.randomUUID().toString();

        DiseaseImageTask task = DiseaseImageTask.builder()
                .taskId(taskId)
                .status(DiseaseStatus.PENDING)
                .requestTime(LocalDateTime.now())
                .message("이미지 분석 요청이 접수되었습니다")
                .build();

        DiseaseImageTaskEntity entity = mapper.domainToEntity(task);
        entity.setBase64Image(convertToBase64(requestDto.getImage()));
        redisRepository.save(entity);

        DiseaseImageMessage message = DiseaseImageMessage.builder()
                .taskId(taskId)
                .requestTime(task.getRequestTime())
                .type("diseaseImageMessage")
                .build();
        producer.sendMessage(message);

        return mapper.domainToResponseDto(task);
    }

    @Override
    public DiseaseImageResultResponseDto getImageAnalysisResult(String taskId) {
        try {
            DiseaseImageTaskEntity entity = redisRepository.findById(taskId)
                .orElseThrow(() -> {
                    log.error("Redis에서 태스크를 찾지 못했습니다: TaskId = {}", taskId);
                    return new TaskNotFoundException("Task not found: " + taskId);
                });

            DiseaseImageTask task = mapper.entityToDomain(entity);

            return mapper.domainToResultResponseDto(task);
        } catch (TaskNotFoundException e) {
            throw e;
        } catch (Exception e) {
            log.error("질병 이미지 분석 결과 조회 중 오류 발생: TaskId = {}, 오류 = {}",
                      taskId, e.getMessage(), e);
            throw e;
        }
    }

    private String convertToBase64(MultipartFile file) throws IOException {
        byte[] fileContent = file.getBytes();
        return Base64.getEncoder().encodeToString(fileContent);
    }
}
