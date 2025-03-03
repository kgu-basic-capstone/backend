package uk.jinhy.server.service.test.example.src.member.controller;

import lombok.Getter;
import uk.jinhy.server.service.test.example.src.member.domain.Member;

@Getter
public class MemberSaveResponseDto {

    private Long id;
    private String email;

    public MemberSaveResponseDto(Long id, String email) {
        this.id = id;
        this.email = email;
    }

    public static MemberSaveResponseDto of(Member member) {
        return new MemberSaveResponseDto(member.getId(), member.getEmail());
    }
}
