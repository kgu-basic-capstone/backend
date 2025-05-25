package uk.jinhy.server.service.disease.application.batch;

import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

@Configuration
@RequiredArgsConstructor
public class DiseaseImageJobConfig {
    private final JobRepository jobRepository;
    private final PlatformTransactionManager transactionManager;
    private final DiseaseImageAnalysisTasklet diseaseImageAnalysisTasklet;

    @Bean
    public Job diseaseImageAnalysisJob() {
        return new JobBuilder("diseaseImageAnalysisJob", jobRepository)
                .start(diseaseImageAnalysisStep())
                .build();
    }

    @Bean
    public Step diseaseImageAnalysisStep() {
        return new StepBuilder("diseaseImageAnalysisStep", jobRepository)
                .tasklet(diseaseImageAnalysisTasklet, transactionManager)
                .build();
    }
}
