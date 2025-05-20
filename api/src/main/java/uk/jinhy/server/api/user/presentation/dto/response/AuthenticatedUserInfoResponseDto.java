package uk.jinhy.server.api.user.presentation.dto.response;

import lombok.Builder;
import lombok.Getter;

@Getter
public class AuthenticatedUserInfoResponseDto {

    private Long id;
    private String username;

    @Builder
    private AuthenticatedUserInfoResponseDto(Long id, String username) {
        this.id = id;
        this.username = username;
    }
}
