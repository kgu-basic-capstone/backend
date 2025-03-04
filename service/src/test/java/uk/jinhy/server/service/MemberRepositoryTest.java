package uk.jinhy.server.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import uk.jinhy.server.service.common.IntegrationTest;
import uk.jinhy.server.service.member.domain.Member;
import uk.jinhy.server.service.member.domain.MemberRepository;

import static org.assertj.core.api.Assertions.assertThat;
import static uk.jinhy.server.service.member.domain.MembershipLevel.BASIC;

@Transactional
@SpringBootTest
class MemberRepositoryTest extends IntegrationTest {

    @Autowired
    MemberRepository memberRepository;

    @DisplayName("이미 가입된 이메일이면 true를 반환한다.")
    @Test
    void alreadyExists() {
        // given
        String email = "test@test.com";

        Member testMember = Member.builder()
                .name("testMember")
                .age(20)
                .email(email)
                .membershipLevel(BASIC)
                .build();

        memberRepository.save(testMember);

        // when
        boolean result = memberRepository.existsByEmail(email);

        // then
        assertThat(result).isTrue();
    }

    @DisplayName("가입되지 않은 이메일이라면 false를 반환한다.")
    @Test
    void notExists() {
        // given
        String email = "test@test.com";

        // when
        boolean result = memberRepository.existsByEmail(email);

        // then
        assertThat(result).isFalse();
    }
}
