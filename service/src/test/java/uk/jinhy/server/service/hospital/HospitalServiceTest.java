package uk.jinhy.server.service.hospital;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import uk.jinhy.server.api.community.domain.*;
import uk.jinhy.server.api.hospital.presentation.HospitalDto;
import uk.jinhy.server.service.community.domain.*;
import uk.jinhy.server.service.hospital.application.HospitalServiceImpl;
import uk.jinhy.server.service.hospital.domain.HospitalEntity;
import uk.jinhy.server.service.hospital.domain.HospitalMapper;
import uk.jinhy.server.service.hospital.domain.HospitalRepository;


import java.util.List;


import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.*;

@ExtendWith(MockitoExtension.class)
class HospitalServiceTest {

    @Mock
    private HospitalRepository hospitalRepository;

    @Mock
    private HospitalMapper hospitalMapper;

    @InjectMocks
    private HospitalServiceImpl hospitalService;

    @Nested
    @DisplayName("병원 목록 조회 테스트")
    class GetHospitalsTest {

        @Test
        @DisplayName("수술 가능 여부 필터링 없이 병원 전체 조회")
        void 병원_전체_조회() {
            // given
            Double latitude = 3.0;
            Double longitude = 3.0;
            Double radius = 5.0;
            Boolean surgeryAvailable = null;
            int page = 0;
            int size = 10;

            HospitalEntity entity1 = mock(HospitalEntity.class);
            HospitalEntity entity2 = mock(HospitalEntity.class);
            List<HospitalEntity> entities = List.of(entity1, entity2);
            Page<HospitalEntity> entityPage = new PageImpl<>(entities);

            HospitalDto.HospitalDetailResponse dto1 = mock(HospitalDto.HospitalDetailResponse.class);
            HospitalDto.HospitalDetailResponse dto2 = mock(HospitalDto.HospitalDetailResponse.class);

            given(hospitalRepository.findByFilters(
                eq(latitude), eq(longitude), eq(radius), eq(surgeryAvailable), any(PageRequest.class))
            ).willReturn(entityPage);

            given(hospitalMapper.toDetailResponse(entity1)).willReturn(dto1);
            given(hospitalMapper.toDetailResponse(entity2)).willReturn(dto2);

            // when
            HospitalDto.HospitalListResponse response =
                hospitalService.getHospitals(latitude, longitude, radius, surgeryAvailable, page, size);

            // then
            assertThat(response).isNotNull();
            assertThat(response.getHospitals()).containsExactly(dto1, dto2);
            assertThat(response.getTotal()).isEqualTo(2);
            assertThat(response.getPage()).isEqualTo(page);
            assertThat(response.getSize()).isEqualTo(size);
        }

        @Test
        @DisplayName("수술 가능 병원만 필터링")
        void 수술_가능_병원_조회() {
            // given
            Boolean surgeryAvailable = true;
            int page = 0;
            int size = 5;

            HospitalEntity entity = mock(HospitalEntity.class);
            HospitalDto.HospitalDetailResponse dto = mock(HospitalDto.HospitalDetailResponse.class);

            Page<HospitalEntity> entityPage = new PageImpl<>(List.of(entity));

            given(hospitalRepository.findByFilters(
                any(), any(), any(), eq(surgeryAvailable), any(PageRequest.class))
            ).willReturn(entityPage);

            given(hospitalMapper.toDetailResponse(entity)).willReturn(dto);

            // when
            HospitalDto.HospitalListResponse response =
                hospitalService.getHospitals(null, null, null, surgeryAvailable, page, size);

            // then
            assertThat(response.getHospitals()).hasSize(1);
        }
    }
}
