package uk.jinhy.server.service.member.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import uk.jinhy.server.api.member.application.MemberService;
import uk.jinhy.server.api.member.domain.Member;
import uk.jinhy.server.service.member.domain.MemberEntity;
import uk.jinhy.server.service.member.domain.MemberMapper;
import uk.jinhy.server.service.member.domain.MemberRepository;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class MemberServiceImpl implements MemberService {
    private final MemberMapper memberMapper;
    private final MemberRepository memberRepository;

    @Transactional
    public Member save(Member newMember) {
        if (isExists(newMember.getEmail())) {
            throw new IllegalArgumentException("이미 가입된 이메일입니다.");
        }

        MemberEntity newEntity = memberMapper.toEntity(newMember);
        MemberEntity savedEntity = memberRepository.save(newEntity);
        return memberMapper.toDomain(savedEntity);
    }

    private boolean isExists(String email) {
        return memberRepository.existsByEmail(email);
    }
}
