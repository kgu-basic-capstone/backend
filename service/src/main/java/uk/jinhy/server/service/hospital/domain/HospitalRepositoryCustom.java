package uk.jinhy.server.service.hospital.domain;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface HospitalRepositoryCustom {
    Page<HospitalEntity> findByFilters(
        Double lat,
        Double lon,
        Double radius,
        Boolean surgeryAvailable,
        Pageable pageable
    );
}
