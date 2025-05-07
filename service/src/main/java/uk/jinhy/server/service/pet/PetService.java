package uk.jinhy.server.service.pet;

import org.springframework.stereotype.Service;

@Service
public interface PetService {
    String getPetNameById(Long petId);
}
