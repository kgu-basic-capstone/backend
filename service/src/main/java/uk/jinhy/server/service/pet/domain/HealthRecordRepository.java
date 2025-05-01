package uk.jinhy.server.service.pet.domain;

import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface HealthRecordRepository extends JpaRepository<HealthRecordEntity, Long> {

    List<HealthRecordEntity> findByPetAndCheckDateAfter(PetEntity pet, LocalDateTime since);
    List<HealthRecordEntity> findByPet(PetEntity pet); // 전체 기록 조회용
    Optional<HealthRecordEntity> findByIdAndPet(Long id, PetEntity pet);

}
