package uk.jinhy.server.service.pet.domain;

import org.springframework.data.jpa.repository.JpaRepository;
import uk.jinhy.server.api.pet.domain.Pet;
import uk.jinhy.server.api.domain.User;

import java.util.List;

public interface PetRepository extends JpaRepository<PetEntity, Long> {
    List<Pet> findByOwner(User owner);
}
