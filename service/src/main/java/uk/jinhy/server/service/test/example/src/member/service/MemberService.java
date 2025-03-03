package uk.jinhy.server.service.test.example.src.member.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import uk.jinhy.server.service.test.example.src.member.domain.Member;
import uk.jinhy.server.service.test.example.src.member.domain.MemberRepository;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class MemberService {

    private final MemberRepository memberRepository;

    @Transactional
    public Member save(Member request) {
        if (isExists(request.getEmail())) {
            throw new IllegalArgumentException("이미 가입된 이메일입니다.");
        }

        return memberRepository.save(request);
    }

    private boolean isExists(String email) {
        return memberRepository.existsByEmail(email);
    }
}
