package uk.jinhy.server.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import uk.jinhy.server.api.member.domain.Member;
import uk.jinhy.server.service.common.IntegrationTest;
import uk.jinhy.server.service.member.domain.MemberEntity;
import uk.jinhy.server.service.member.domain.MemberRepository;
import uk.jinhy.server.service.member.service.MemberServiceImpl;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@Transactional
@SpringBootTest
class MemberEntityServiceTest extends IntegrationTest {

    @Autowired
    MemberServiceImpl memberService;

    @Autowired
    MemberRepository memberRepository;

    @DisplayName("회원가입 시 이름, 나이, 이메일이 필요하며 가입 완료 시 회원 정보를 반환한다.")
    @Test
    void saveNewMember() {
        // given
        String email = "test@test.com";
        Member request = Member.builder()
                .name("testMember")
                .age(20)
                .email(email)
                .build();

        // when
        Member savedMember = memberService.save(request);

        // then
        assertThat(savedMember)
                .extracting(Member::getEmail)
                .isEqualTo(email);
    }

    @DisplayName("이미 가입된 이메일로 회원가입을 시도할 경우 예외가 발생한다.")
    @Test
    void alreadyExistsMember() {
        // given
        String email = "test@test.com";
        MemberEntity memberEntity = MemberEntity.builder()
                .email(email)
                .build();
        Member member = Member.builder()
                .email(email)
                .build();

        memberRepository.save(memberEntity);

        // when & then
        assertThatThrownBy(() -> memberService.save(member))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("이미 가입된 이메일입니다.");
    }
}
