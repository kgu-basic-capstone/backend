package uk.jinhy.server.api.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import uk.jinhy.server.api.hospital.domain.Hospital;
import uk.jinhy.server.api.hospital.domain.HospitalReservation;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

class HospitalReservationTest {
    @DisplayName("예약 상태를 변경할 수 있다.")
    @Test
    void 예약_상태_변경() {
        // given
        HospitalReservation sut = HospitalReservation.builder()
            .user(UserFactory.create().build())
            .pet(PetFactory.create().build())
            .hospital(Hospital.builder().name("테스트 병원").build())
            .reservationDateTime(LocalDateTime.now())
            .status(HospitalReservation.ReservationStatus.PENDING)
            .build();

        // when
        sut.changeStatus(HospitalReservation.ReservationStatus.CONFIRMED);

        // then
        assertThat(sut.getStatus()).isEqualTo(HospitalReservation.ReservationStatus.CONFIRMED);
    }
}
