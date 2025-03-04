package uk.jinhy.server.service.member.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import uk.jinhy.server.service.global.api.ApiResponse;
import uk.jinhy.server.service.member.domain.Member;
import uk.jinhy.server.service.member.service.MemberService;

@RequiredArgsConstructor
@RestController
public class MemberController {

    private final MemberService memberService;

    @PostMapping("/members")
    public ApiResponse<MemberSaveResponseDto> save(@Valid @RequestBody MemberSaveRequestDto request) {
        Member member = memberService.save(request.toEntity());
        return ApiResponse.success(MemberSaveResponseDto.of(member));
    }
}
