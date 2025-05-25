package uk.jinhy.server.service.vaccination.presentation;

import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.JpaRepository;
import uk.jinhy.server.service.domain.VaccinationEntity;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;


public interface VaccinationRepository extends JpaRepository<VaccinationEntity, Long> {

    // 특정 Pet의 백신 기록 조회
    List<VaccinationEntity> findByPetId(Long petId);

    // 특정 Pet의 완료/미완료 백신 조회
    List<VaccinationEntity> findByPetIdAndIsCompleted(Long petId, boolean isCompleted);

    // 특정 Pet의 특정 날짜 이후 예정된 백신 조회
    List<VaccinationEntity> findByPetIdAndVaccinationDateAfter(Long petId, LocalDate date);

    // 특정 사용자의 모든 반려동물 백신 조회
    @Query("SELECT v FROM VaccinationEntity v WHERE v.pet.owner.id = :userId")
    List<VaccinationEntity> findByPetOwnerId(@Param("userId") Long userId);

    // 특정 사용자의 모든 반려동물 완료/미완료 백신 조회
    @Query("SELECT v FROM VaccinationEntity v WHERE v.pet.owner.id = :userId AND v.isCompleted = :isCompleted")
    List<VaccinationEntity> findByPetOwnerIdAndIsCompleted(@Param("userId") Long userId, @Param("isCompleted") Boolean isCompleted);

    // 특정 사용자의 모든 반려동물 예정된 백신 조회
    @Query("SELECT v FROM VaccinationEntity v WHERE v.pet.owner.id = :userId AND v.vaccinationDate > :date")
    List<VaccinationEntity> findByPetOwnerIdAndVaccinationDateAfter(@Param("userId") Long userId, @Param("date") LocalDate date);
}
