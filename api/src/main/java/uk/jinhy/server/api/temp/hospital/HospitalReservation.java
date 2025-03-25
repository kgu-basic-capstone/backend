package uk.jinhy.server.api.temp.hospital;

import uk.jinhy.server.api.domain.Pet;
import uk.jinhy.server.api.domain.User;

import java.time.LocalDateTime;

public class HospitalReservation {

    private Long id;
    private User user;
    private Pet pet;
    private Hospital hospital;
    private LocalDateTime reservationDateTime;
    private String purpose;
    private String status; // 예약 상태 (확정, 대기, 취소 등)

}
