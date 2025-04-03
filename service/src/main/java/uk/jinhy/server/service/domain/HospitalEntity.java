package uk.jinhy.server.service.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "hospitals")
public class HospitalEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String address;

    @Column(nullable = false)
    private String phoneNumber;

    private Double rating;

    private Boolean surgeryAvailable;

    private String operatingHours;

    private Double latitude;

    private Double longitude;

    @OneToMany(mappedBy = "hospital", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<HospitalReservationEntity> reservations = new ArrayList<>();

    @Builder
    public HospitalEntity(String name, String address, String phoneNumber,
                          Double rating, Boolean surgeryAvailable,
                          String operatingHours, Double latitude, Double longitude) {
        this.name = name;
        this.address = address;
        this.phoneNumber = phoneNumber;
        this.rating = rating;
        this.surgeryAvailable = surgeryAvailable;
        this.operatingHours = operatingHours;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public HospitalReservationEntity addReservation(HospitalReservationEntity reservation) {
        this.reservations.add(reservation);
        return reservation;
    }
}
