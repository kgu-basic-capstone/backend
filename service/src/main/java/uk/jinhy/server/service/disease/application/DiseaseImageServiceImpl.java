package uk.jinhy.server.service.disease.application;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import uk.jinhy.server.api.disease.application.DiseaseImageService;
import uk.jinhy.server.api.disease.domain.DiseaseImageTask;
import uk.jinhy.server.api.disease.domain.DiseaseStatus;
import uk.jinhy.server.api.disease.domain.exception.TaskNotFoundException;
import uk.jinhy.server.api.disease.domain.exception.ImageProcessingException;
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
        log.info("새로운 질병 이미지 분석 태스크 생성: taskId={}", taskId);

        try {
            DiseaseImageTask task = DiseaseImageTask.builder()
                .taskId(taskId)
                .status(DiseaseStatus.PENDING)
                .requestTime(LocalDateTime.now())
                .message("이미지 분석 요청이 접수되었습니다")
                .build();

            DiseaseImageTaskEntity entity = mapper.domainToEntity(task);
            entity.setBase64Image(convertToBase64(requestDto.getImage()));
            redisRepository.save(entity);
            log.info("Redis에 태스크 정보 저장 완료: taskId={}", taskId);

            DiseaseImageMessage message = DiseaseImageMessage.builder()
                .taskId(taskId)
                .requestTime(task.getRequestTime())
                .type("diseaseImageMessage")
                .build();
            producer.sendMessage(message);
            log.info("메시지 큐에 분석 요청 전송 완료: taskId={}", taskId);

            return mapper.domainToResponseDto(task);
        } catch (IOException e) {
            log.error("이미지 변환 중 오류 발생: taskId={}", taskId, e);
            throw new ImageProcessingException("이미지 처리 중 오류가 발생했습니다: " + e.getMessage());
        } catch (Exception e) {
            log.error("질병 이미지 분석 요청 처리 중 오류 발생: taskId={}", taskId, e);
            throw new RuntimeException("질병 이미지 분석 요청 처리 중 오류가 발생했습니다", e);
        }
    }

    @Override
    public DiseaseImageResultResponseDto getImageAnalysisResult(String taskId) {
        log.info("질병 이미지 분석 결과 조회: taskId={}", taskId);
        try {
            DiseaseImageTaskEntity entity = redisRepository.findById(taskId)
                .orElseThrow(() -> {
                    log.error("Redis에서 태스크를 찾지 못했습니다: taskId={}", taskId);
                    return new TaskNotFoundException("태스크를 찾을 수 없습니다: " + taskId);
                });

            DiseaseImageTask task = mapper.entityToDomain(entity);
            log.info("질병 이미지 분석 결과 조회 완료: taskId={}, status={}",
                taskId, task.getStatus());

            return mapper.domainToResultResponseDto(task);
        } catch (TaskNotFoundException e) {
            throw e;
        } catch (Exception e) {
            log.error("질병 이미지 분석 결과 조회 중 오류 발생: taskId={}", taskId, e);
            throw new RuntimeException("질병 이미지 분석 결과 조회 중 오류가 발생했습니다", e);
        }
    }

    private String convertToBase64(MultipartFile file) throws IOException {
        try {
            byte[] fileContent = file.getBytes();
            return Base64.getEncoder().encodeToString(fileContent);
        } catch (IOException e) {
            log.error("이미지 Base64 변환 중 오류 발생", e);
            throw e;
        }
    }
}
