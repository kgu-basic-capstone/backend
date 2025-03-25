package uk.jinhy.server.api.domain.pet;

import lombok.Builder;
import lombok.Getter;
import uk.jinhy.server.api.domain.user.User;

import java.time.LocalDate;

@Getter
public class Pet {

    private Long id;
    private User owner;
    private String name;
    private LocalDate birthDate;

    @Builder
    private Pet(User owner, String name, LocalDate birthDate) {
        this.owner = owner;
        this.name = name;
        this.birthDate = birthDate;
    }
}
