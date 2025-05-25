package uk.jinhy.server.service.member.domain;

import org.mapstruct.Mapper;
import uk.jinhy.server.api.member.domain.Member;

@Mapper(componentModel = "spring")
public interface MemberMapper {
    MemberEntity toEntity(Member member);
    Member toDomain(MemberEntity memberEntity);
}
