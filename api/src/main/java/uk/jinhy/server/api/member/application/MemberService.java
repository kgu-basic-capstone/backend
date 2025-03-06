package uk.jinhy.server.api.member.application;

import uk.jinhy.server.api.member.domain.Member;

public interface MemberService {
    Member save(Member request);
}
