package uk.jinhy.server.service.hospital.domain;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import uk.jinhy.server.api.hospital.domain.Hospital;
import uk.jinhy.server.api.hospital.domain.HospitalReservation;
import uk.jinhy.server.api.hospital.presentation.HospitalDto.HospitalDetailResponse;
import uk.jinhy.server.api.hospital.presentation.HospitalDto.HospitalReservationResponse;

@Mapper(componentModel = "spring")
public interface HospitalMapper {

    // 1. Domain ↔ Entity 변환 (기본 Mapper 역할)
    HospitalEntity toEntity(Hospital hospital);
    Hospital toDomain(HospitalEntity hospitalEntity);

    // 2. Entity → Response DTO 변환 (기존 수동 매핑 메서드 대체)
    HospitalDetailResponse toDetailResponse(HospitalEntity hospital);

    // 3. HospitalReservation 관련 매핑
    @Mapping(target = "petId", source = "pet.id")
    @Mapping(target = "petName", source = "pet.name")
    @Mapping(target = "hospitalId", source = "hospitalEntity.id")
    @Mapping(target = "hospitalName", source = "hospitalEntity.name")
    @Mapping(target = "status", expression = "java(reservation.getStatus().name())")
    HospitalReservationResponse toReservationResponse(HospitalReservationEntity reservation);

    // 4. Request DTO → Entity 변환 (필요시)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    HospitalEntity toEntity(uk.jinhy.server.api.hospital.presentation.HospitalDto.HospitalReservationRequest request);
}
