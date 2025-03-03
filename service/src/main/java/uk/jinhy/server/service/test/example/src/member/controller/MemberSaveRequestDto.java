package uk.jinhy.server.service.test.example.src.member.controller;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Getter;
import uk.jinhy.server.service.test.example.src.member.domain.Member;

@Getter
public class MemberSaveRequestDto {

    @NotBlank(message = "이름을 입력해주세요.")
    private String name;

    @Min(value = 0, message = "음수는 입력할 수 없습니다.")
    private int age;

    @Email(message = "이메일 형식에 맞게 입력해주세요.")
    private String email;

    public Member toEntity() {
        return Member.builder()
                .name(name)
                .age(age)
                .email(email)
                .build();
    }

    @Builder
    private MemberSaveRequestDto(String name, int age, String email) {
        this.name = name;
        this.age = age;
        this.email = email;
    }
}
