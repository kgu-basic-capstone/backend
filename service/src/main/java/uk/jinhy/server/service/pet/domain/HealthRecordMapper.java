package uk.jinhy.server.service.pet.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import uk.jinhy.server.api.pet.domain.HealthRecord;

import java.time.LocalDateTime;

@Mapper(componentModel = "spring", uses = PetMapper.class)
public interface HealthRecordMapper {

    @Mapping(source = "pet", target = "pet")
    HealthRecordEntity toEntity(HealthRecord domain);

    @Mapping(source = "pet", target = "pet")
    HealthRecord toDomain(HealthRecordEntity entity);

}
