package uk.jinhy.server.service.pet.domain;

import org.mapstruct.Mapper;
import uk.jinhy.server.api.pet.domain.Pet;
import uk.jinhy.server.service.user.domain.UserMapper;

@Mapper(componentModel = "spring", uses = UserMapper.class)
public interface PetMapper {
    PetEntity toEntity(Pet pet);
    Pet toDomain(PetEntity petEntity);
}
