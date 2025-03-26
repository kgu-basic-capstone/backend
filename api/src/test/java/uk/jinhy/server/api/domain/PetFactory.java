package uk.jinhy.server.api.domain;

import java.time.LocalDate;

public class PetFactory {

    public static Pet.PetBuilder create() {
        return Pet.builder()
            .owner(UserFactory.create().build())
            .name("testPet")
            .birthDate(LocalDate.now());
    }

}
