package uk.jinhy.server.service.hospital.domain;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReservationRepository extends JpaRepository<HospitalReservationEntity, Long> {

    List<HospitalReservationEntity> findByUserIdOrderByReservationDateTimeDesc(Long userId);

    List<HospitalReservationEntity> findByUserIdAndStatusOrderByReservationDateTimeDesc(Long userId, String status);

    List<HospitalReservationEntity> findByPetId(Long petId);

    List<HospitalReservationEntity> findByHospitalId(Long hospitalId);
}
