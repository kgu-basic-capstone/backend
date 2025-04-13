package uk.jinhy.server.service.disease.infrastructure;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Component;
import uk.jinhy.server.service.disease.application.DiseaseImageMessage;
import uk.jinhy.server.service.disease.application.DiseaseImageProducer;

import java.util.concurrent.CompletableFuture;

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
            return;
        }

        String taskId = message.getTaskId();

        try {
            CompletableFuture<SendResult<String, DiseaseImageMessage>> future =
                kafkaTemplate.send(TOPIC, taskId, message);

            future.whenComplete((result, ex) -> {
                if (ex != null) {
                    log.error("메시지 전송 실패: TaskId = {}, 오류 = {}", taskId, ex.getMessage(), ex);
                }
            });
        } catch (Exception e) {
            log.error("메시지 전송 중 예외 발생: TaskId = {}, 오류 = {}", taskId, e.getMessage(), e);
        }
    }
}
