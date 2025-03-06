package uk.jinhy.server.service.member.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import uk.jinhy.server.api.member.domain.Member;
import uk.jinhy.server.api.member.presentation.MemberDto;
import uk.jinhy.server.service.member.service.MemberServiceImpl;
import uk.jinhy.server.api.common.ApiResponse;

@RequiredArgsConstructor
@RestController
public class MemberControllerImpl {

    private final MemberServiceImpl memberService;

    @PostMapping("/members")
    public ApiResponse<MemberDto.MemberSaveResponseDto> save(@Valid @RequestBody MemberDto.MemberSaveRequestDto request) {
        Member member = memberService.save(request.toEntity());
        return ApiResponse.success(MemberDto.MemberSaveResponseDto.of(member));
    }
}
