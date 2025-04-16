package uk.jinhy.server.service.disease.infrastructure;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Component;
import uk.jinhy.server.api.disease.domain.exception.MessageDeliveryException;
import uk.jinhy.server.service.disease.application.DiseaseImageMessage;
import uk.jinhy.server.service.disease.application.DiseaseImageProducer;

import java.util.concurrent.ExecutionException;

@Slf4j
@RequiredArgsConstructor
@Component
public class DiseaseImageProducerImpl implements DiseaseImageProducer {
    private final KafkaTemplate<String, DiseaseImageMessage> kafkaTemplate;

    private static final String TOPIC = "disease-image-analysis";

    @Override
    public void sendMessage(DiseaseImageMessage message) {
        if (message == null || message.getTaskId() == null) {
            log.error("메시지 또는 TaskId가 null입니다");
            throw new IllegalArgumentException("메시지 또는 TaskId는 null일 수 없습니다");
        }

        String taskId = message.getTaskId();
        log.info("질병 이미지 분석 메시지 전송 시작: taskId={}, type={}, requestTime={}", 
                taskId, message.getType(), message.getRequestTime());

        try {
            log.debug("Kafka 토픽 '{}' 으로 메시지 전송 시도: taskId={}", TOPIC, taskId);
            
            SendResult<String, DiseaseImageMessage> result = 
                kafkaTemplate.send(TOPIC, taskId, message).get();
            
            log.info("메시지 전송 성공: taskId={}, topic={}, partition={}, offset={}", 
                    taskId, 
                    result.getRecordMetadata().topic(),
                    result.getRecordMetadata().partition(),
                    result.getRecordMetadata().offset());

        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            log.error("메시지 전송 중 인터럽트 발생: taskId={}, topic={}", taskId, TOPIC, e);
            throw new MessageDeliveryException(
                String.format("메시지 전송이 중단되었습니다 (taskId=%s)", taskId), 
                e
            );
        } catch (ExecutionException e) {
            log.error("메시지 전송 실패: taskId={}, topic={}, 오류={}", 
                    taskId, TOPIC, e.getCause().getMessage(), e.getCause());
            throw new MessageDeliveryException(
                String.format("메시지 전송에 실패했습니다 (taskId=%s): %s", taskId, e.getCause().getMessage()), 
                e.getCause()
            );
        } catch (Exception e) {
            log.error("메시지 전송 중 예외 발생: taskId={}, topic={}, 오류={}", 
                    taskId, TOPIC, e.getMessage(), e);
            throw new MessageDeliveryException(
                String.format("메시지 전송 중 오류가 발생했습니다 (taskId=%s): %s", taskId, e.getMessage()), 
                e
            );
        }
    }
}
