package uk.jinhy.server.service.member.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import lombok.*;
import uk.jinhy.server.api.member.domain.MembershipLevel;

import static uk.jinhy.server.api.member.domain.MembershipLevel.*;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
@Entity
public class MemberEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private int age;

    @Email
    private String email;

    @Enumerated(EnumType.STRING)
    private MembershipLevel membershipLevel = BASIC;
}
