package uk.jinhy.server.service.disease.application;

import org.springframework.stereotype.Component;

@Component
public interface DiseaseImageProducer {
    void sendMessage(DiseaseImageMessage message);
}
