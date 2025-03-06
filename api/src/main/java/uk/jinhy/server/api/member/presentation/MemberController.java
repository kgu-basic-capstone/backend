package uk.jinhy.server.api.member.presentation;

import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import uk.jinhy.server.api.common.ApiResponse;

public interface MemberController {
    @PostMapping("/members")
    ApiResponse<MemberDto.MemberSaveResponseDto> save(@Valid @RequestBody MemberDto.MemberSaveRequestDto request);
}
