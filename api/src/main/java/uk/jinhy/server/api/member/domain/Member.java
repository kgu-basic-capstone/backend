package uk.jinhy.server.api.member.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

// TODO:
// 임시로 Getter, AllArgs 사용, 도메인 로직에 맞게 변경 필요
@Getter
@AllArgsConstructor
@Builder
public class Member {
    private Long id;
    private String name;
    private int age;
    private String email;
    @Builder.Default
    private MembershipLevel membershipLevel = MembershipLevel.BASIC;
}
