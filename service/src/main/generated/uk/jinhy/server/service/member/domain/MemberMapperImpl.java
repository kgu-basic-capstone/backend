package uk.jinhy.server.service.member.domain;

import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;
import uk.jinhy.server.api.member.domain.Member;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-05-20T13:13:32+0000",
    comments = "version: 1.6.3, compiler: IncrementalProcessingEnvironment from gradle-language-java-8.5.jar, environment: Java 17.0.14 (Microsoft)"
)
@Component
public class MemberMapperImpl implements MemberMapper {

    @Override
    public MemberEntity toEntity(Member member) {
        if ( member == null ) {
            return null;
        }

        MemberEntity.MemberEntityBuilder memberEntity = MemberEntity.builder();

        memberEntity.id( member.getId() );
        memberEntity.name( member.getName() );
        memberEntity.age( member.getAge() );
        memberEntity.email( member.getEmail() );
        memberEntity.membershipLevel( member.getMembershipLevel() );

        return memberEntity.build();
    }

    @Override
    public Member toDomain(MemberEntity memberEntity) {
        if ( memberEntity == null ) {
            return null;
        }

        Member.MemberBuilder member = Member.builder();

        member.id( memberEntity.getId() );
        member.name( memberEntity.getName() );
        member.age( memberEntity.getAge() );
        member.email( memberEntity.getEmail() );
        member.membershipLevel( memberEntity.getMembershipLevel() );

        return member.build();
    }
}
