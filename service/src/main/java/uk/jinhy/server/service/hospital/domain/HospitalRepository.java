package uk.jinhy.server.service.hospital.domain;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface HospitalRepository extends JpaRepository<HospitalEntity, Long> {

    @Query(value = "SELECT * FROM hospitals h " +
        "WHERE (:surgeryAvailable IS NULL OR h.surgery_available = :surgeryAvailable) " +
        "AND (:lat IS NULL OR :lon IS NULL OR :radius IS NULL OR " +
        "(6371 * acos(cos(radians(:lat)) * cos(radians(h.latitude)) * cos(radians(h.longitude) - radians(:lon)) + sin(radians(:lat)) * sin(radians(h.latitude)))) <= :radius)",
        nativeQuery = true)
    Page<HospitalEntity> findByFilters(
        @Param("surgeryAvailable") Boolean surgeryAvailable,
        @Param("lat") Double latitude,
        @Param("lon") Double longitude,
        @Param("radius") Double radius,
        Pageable pageable);
}
