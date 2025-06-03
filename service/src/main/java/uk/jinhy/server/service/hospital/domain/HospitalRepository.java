package uk.jinhy.server.service.hospital.domain;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface HospitalRepository extends JpaRepository<HospitalEntity, Long>, HospitalRepositoryCustom {


}
