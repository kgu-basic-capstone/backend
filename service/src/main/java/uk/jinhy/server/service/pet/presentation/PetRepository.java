package uk.jinhy.server.service.pet.presentation;

import org.springframework.data.jpa.repository.JpaRepository;
import uk.jinhy.server.service.domain.PetEntity;

import java.util.Optional;

public interface PetRepository extends JpaRepository<PetEntity, Long> {

    // VaccinationServic 동작으로 인해 임시로 만든 메서드
    Optional<PetEntity> findById(Long petId);
    boolean existsById(Long petId);
}
