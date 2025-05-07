package uk.jinhy.server.service.pet.domain;

import org.springframework.data.jpa.repository.JpaRepository;
import uk.jinhy.server.api.domain.Pet;

public interface PetRepository extends JpaRepository<Pet, Long> {
}
