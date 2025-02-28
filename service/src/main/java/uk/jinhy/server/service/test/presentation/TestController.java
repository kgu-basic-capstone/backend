package uk.jinhy.server.service.test.presentation;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.bind.annotation.*;
import uk.jinhy.server.api.domain.Test;
import uk.jinhy.server.service.test.domain.TestMapper;
import uk.jinhy.server.service.test.domain.TestRepository;

import java.time.Duration;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/")
@RequiredArgsConstructor
public class TestController {
    private final TestRepository testRepository;
    private final TestMapper testMapper;

    private final RedisTemplate<String, Object> redisTemplate;
    private final KafkaTemplate<String, String> kafkaTemplate;
    private final ObjectMapper objectMapper;

    private static final String REDIS_PAGE_KEY = "test:page:";
    private static final Duration CACHE_TTL = Duration.ofSeconds(10);
    private static final String KAFKA_TOPIC = "test-write-topic";

    @GetMapping
    public ResponseEntity<List<Test>> getPage(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        return ResponseEntity.ok(List.of());
    }

    @PostMapping
    public ResponseEntity<String> create() {
        return ResponseEntity.ok("Message queued for processing");
    }
}
