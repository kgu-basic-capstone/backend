package uk.jinhy.server.service.domain;

import uk.jinhy.server.service.user.domain.UserEntity;

import uk.jinhy.server.service.pet.domain.PetEntity;

import java.time.LocalDate;

public class PetFactory {
    private UserEntity owner;
    private String name;
    private LocalDate birthDate;

    private PetFactory() {
        this.owner = UserFactory.create().build();
        this.name = "DefaultPet";
        this.birthDate = LocalDate.now().minusYears(1);
    }

    public static PetFactory create() {
        return new PetFactory();
    }

    public PetFactory owner(UserEntity owner) {
        this.owner = owner;
        return this;
    }

    public PetFactory name(String name) {
        this.name = name;
        return this;
    }

    public PetFactory birthDate(LocalDate birthDate) {
        this.birthDate = birthDate;
        return this;
    }

    public PetEntity build() {
        return PetEntity.builder()
            .owner(this.owner)
            .name(this.name)
            .birthDate(this.birthDate)
            .build();
    }
}
