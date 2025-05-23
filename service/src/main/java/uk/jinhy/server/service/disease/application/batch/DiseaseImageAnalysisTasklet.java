package uk.jinhy.server.service.disease.application.batch;

import jakarta.annotation.PreDestroy;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.errors.SerializationException;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.support.serializer.ErrorHandlingDeserializer;
import org.springframework.kafka.support.serializer.JsonDeserializer;
import org.springframework.stereotype.Component;
import uk.jinhy.server.api.disease.domain.DiseaseStatus;
import uk.jinhy.server.service.disease.application.AiModelClient;
import uk.jinhy.server.service.disease.domain.DiseaseImageTaskEntity;
import uk.jinhy.server.service.disease.domain.DiseaseImageRedisRepository;
import uk.jinhy.server.service.disease.application.DiseaseImageMessage;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.Iterator;
import java.util.Properties;

@Slf4j
@Component
@StepScope
@RequiredArgsConstructor
public class DiseaseImageAnalysisTasklet implements Tasklet {
    private final DiseaseImageRedisRepository redisRepository;
    private final AiModelClient aiModelClient;

    @Value("${spring.kafka.bootstrap-servers}")
    private String bootstrapServers;

    private KafkaConsumer<String, DiseaseImageMessage> consumer;
    private Iterator<ConsumerRecord<String, DiseaseImageMessage>> recordIterator;

    @Override
    public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
        DiseaseImageTaskEntity item = readFromKafka();
        if (item == null) {
            log.info("카프카로부터 전달 받은 아이템 없음");
            return RepeatStatus.FINISHED;
        }
        log.info("카프카로부터 전달 받은 아이템 TaskId: {}", item.getTaskId());

        try {
            item.setStatus(DiseaseStatus.PROCESSING);
            redisRepository.save(item);

            item.setResults(aiModelClient.analyzeImage(item.getBase64Image()));
            item.setStatus(DiseaseStatus.COMPLETED);
            item.setResultTime(LocalDateTime.now());
            redisRepository.save(item);
        } catch (Exception e) {
            log.error("질병 이미지 분석 처리 중 오류 발생: {}", e.getMessage(), e);
            item.setStatus(DiseaseStatus.FAILED);
            item.setErrorMessage(e.getMessage());
            item.setResultTime(LocalDateTime.now());
            redisRepository.save(item);
        }

        return RepeatStatus.CONTINUABLE;
    }

    private DiseaseImageTaskEntity readFromKafka() {
        try {
            createNewConsumer();

            if (recordIterator == null || !recordIterator.hasNext()) {
                ConsumerRecords<String, DiseaseImageMessage> records = consumer.poll(Duration.ofMillis(1000));

                if (records.isEmpty()) {
                    return null;
                }

                recordIterator = records.iterator();
            }

            if (recordIterator.hasNext()) {
                try {
                    ConsumerRecord<String, DiseaseImageMessage> record = recordIterator.next();

                    if (record.value() == null) {
                        consumer.commitSync();
                        return null;
                    }

                    DiseaseImageMessage message = record.value();
                    String taskId = message.getTaskId();

                    if (taskId == null) {
                        consumer.commitSync();
                        return null;
                    }

                    DiseaseImageTaskEntity entity = redisRepository.findById(taskId).orElse(null);
                    if (entity == null) {
                        log.error("Redis에서 태스크를 찾지 못했습니다: TaskId = {}", taskId);
                    }

                    consumer.commitSync();
                    return entity;
                } catch (SerializationException e) {
                    log.error("메시지 역직렬화 예외 발생", e);
                    consumer.commitSync();
                    return null;
                }
            }
        } catch (Exception e) {
            log.error("메시지 읽기 중 오류 발생: {}", e.getMessage(), e);
        }

        return null;
    }

    private void createNewConsumer() {
        if (consumer == null) {
            try {
                Properties props = new Properties();
                props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
                props.put(ConsumerConfig.GROUP_ID_CONFIG, "disease-batch-consumer");
                props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");
                props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
                props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, ErrorHandlingDeserializer.class.getName());
                props.put(ErrorHandlingDeserializer.VALUE_DESERIALIZER_CLASS, JsonDeserializer.class.getName());
                props.put(JsonDeserializer.TRUSTED_PACKAGES, "*");
                props.put(JsonDeserializer.TYPE_MAPPINGS, "diseaseImageMessage:uk.jinhy.server.service.disease.application.DiseaseImageMessage");
                props.put(JsonDeserializer.VALUE_DEFAULT_TYPE, "uk.jinhy.server.service.disease.application.DiseaseImageMessage");
                props.put(JsonDeserializer.REMOVE_TYPE_INFO_HEADERS, false);
                props.put(ConsumerConfig.MAX_POLL_RECORDS_CONFIG, "1");
                props.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, "false");

                consumer = new KafkaConsumer<>(props);
                consumer.subscribe(Collections.singletonList("disease-image-analysis"));
            } catch (Exception e) {
                log.error("Kafka 컨슈머 초기화 중 오류 발생", e);
                throw e;
            }
        }
    }

    @PreDestroy
    public void close() {
        if (consumer != null) {
            try {
                consumer.close();
            } catch (Exception e) {
                log.error("Kafka 컨슈머 리소스 해제 중 오류 발생", e);
            }
        }
    }
}
