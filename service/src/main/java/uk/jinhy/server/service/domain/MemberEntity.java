package uk.jinhy.server.service.domain;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
//VaccinationServiceTest로 인한 임시 MemberEntity
@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "members") // 임시 테이블명
public class MemberEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String email;

    private String name;

    private int age;

    @Builder
    public MemberEntity(String email, String name, int age) {
        this.email = email;
        this.name = name;
        this.age = age;
    }
}
