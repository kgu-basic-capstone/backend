package uk.jinhy.server.service.disease.application.batch;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.integration.redis.util.RedisLockRegistry;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;

@Slf4j
@Component
@EnableScheduling
@RequiredArgsConstructor
public class DiseaseImageJobLauncher {
    private final JobLauncher jobLauncher;
    private final Job diseaseImageAnalysisJob;
    private final RedisLockRegistry redisLockRegistry;

    private static final String DISEASE_ANALYSIS_JOB_LOCK = "disease-analysis-job-lock";

    @Scheduled(fixedDelay = 500)
    public void launchJob() {
        Lock lock = null;
        boolean acquired = false;

        try {
            lock = redisLockRegistry.obtain(DISEASE_ANALYSIS_JOB_LOCK);
            if (lock == null) {
                log.error("분산락 획득 실패: Redis 락 레지스트리에서 락을 가져올 수 없습니다");
                return;
            }

            acquired = lock.tryLock(1, TimeUnit.SECONDS);

            if (acquired) {
                JobParameters jobParameters = new JobParametersBuilder()
                    .addDate("launchDate", new Date())
                    .toJobParameters();

                try {
                    jobLauncher.run(diseaseImageAnalysisJob, jobParameters);
                } catch (Exception e) {
                    log.error("질병 분석 작업 실행 중 오류가 발생했습니다", e);
                }
            }
        } catch (InterruptedException e) {
            log.error("질병 분석 작업 준비 중 오류가 발생했습니다", e);
        } finally {
            if (acquired && lock != null) {
                try {
                    lock.unlock();
                } catch (Exception e) {
                    log.error("분산락 해제 중 오류 발생", e);
                }
            }
        }
    }

    @Bean
    public CommandLineRunner runBatchOnStartup() {
        return args -> {
            launchJob();
        };
    }
}
