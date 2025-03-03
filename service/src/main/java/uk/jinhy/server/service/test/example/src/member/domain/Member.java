package uk.jinhy.server.service.test.example.src.member.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import static uk.jinhy.server.service.test.example.src.member.domain.MembershipLevel.*;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class Member {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private int age;

    @Email
    private String email;

    @Enumerated(EnumType.STRING)
    private MembershipLevel membershipLevel = BASIC;

    @Builder
    private Member(String name, int age, String email, MembershipLevel membershipLevel) {
        this.name = name;
        this.age = age;
        this.email = email;
        this.membershipLevel = membershipLevel;
    }
}
