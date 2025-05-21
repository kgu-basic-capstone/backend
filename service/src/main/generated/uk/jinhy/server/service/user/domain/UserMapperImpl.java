package uk.jinhy.server.service.user.domain;

import java.util.ArrayList;
import java.util.List;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;
import uk.jinhy.server.api.pet.domain.Pet;
import uk.jinhy.server.api.user.domain.User;
import uk.jinhy.server.service.pet.domain.PetEntity;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-05-20T13:13:32+0000",
    comments = "version: 1.6.3, compiler: IncrementalProcessingEnvironment from gradle-language-java-8.5.jar, environment: Java 17.0.14 (Microsoft)"
)
@Component
public class UserMapperImpl implements UserMapper {

    @Override
    public UserEntity toEntity(User user) {
        if ( user == null ) {
            return null;
        }

        UserEntity.UserEntityBuilder userEntity = UserEntity.builder();

        userEntity.username( user.getUsername() );

        return userEntity.build();
    }

    @Override
    public User toDomain(UserEntity userEntity) {
        if ( userEntity == null ) {
            return null;
        }

        User.UserBuilder user = User.builder();

        user.id( userEntity.getId() );
        user.username( userEntity.getUsername() );
        user.email( userEntity.getEmail() );
        user.pets( petEntityListToPetList( userEntity.getPets() ) );

        return user.build();
    }

    protected Pet petEntityToPet(PetEntity petEntity) {
        if ( petEntity == null ) {
            return null;
        }

        Pet.PetBuilder pet = Pet.builder();

        pet.owner( toDomain( petEntity.getOwner() ) );
        pet.name( petEntity.getName() );
        pet.birthDate( petEntity.getBirthDate() );

        return pet.build();
    }

    protected List<Pet> petEntityListToPetList(List<PetEntity> list) {
        if ( list == null ) {
            return null;
        }

        List<Pet> list1 = new ArrayList<Pet>( list.size() );
        for ( PetEntity petEntity : list ) {
            list1.add( petEntityToPet( petEntity ) );
        }

        return list1;
    }
}
