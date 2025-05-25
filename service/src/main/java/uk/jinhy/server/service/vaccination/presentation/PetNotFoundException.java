package uk.jinhy.server.service.vaccination.presentation;

import java.io.Serializable;

public class PetNotFoundException extends RuntimeException  {

    public PetNotFoundException(String message) {
        super(message);
    }

    public PetNotFoundException(Long petId) {
        super("Pet not found with id: " + petId);
    }
}

