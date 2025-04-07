package uk.jinhy.server.api.community.presentation.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;

import java.time.LocalDateTime;

@Data
public class CommunityCommentResponseDto {
    private Long id;
    private String content;
    private AuthorInfo author;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createdAt;

    @Data
    public static class AuthorInfo {
        private Long id;
        private String username;
    }
}
