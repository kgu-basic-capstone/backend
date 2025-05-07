package uk.jinhy.server.service.hospital.domain;

import jakarta.persistence.*;
import lombok.*;
import uk.jinhy.server.api.domain.Pet;
import uk.jinhy.server.api.domain.User;

import java.time.LocalDateTime;

@Entity
@Table(name = "hospital_reservations")
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class HospitalReservationEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 예약한 사용자
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    // 예약한 반려동물
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "pet_id", nullable = false)
    private Pet pet;

    // 예약 대상 병원
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "hospital_id", nullable = false)
    private HospitalEntity hospitalEntity;

    @Column(name = "reservation_date_time", nullable = false)
    private LocalDateTime reservationDateTime;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ReservationStatus status;

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
    }

    public void changeStatus(ReservationStatus newStatus) {
        this.status = newStatus;
    }

    public enum ReservationStatus {
        PENDING, CONFIRMED, CANCELLED
    }
}
