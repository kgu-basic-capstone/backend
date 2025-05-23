package uk.jinhy.server.api.community.presentation.dto.response;


import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class CommunityPostDetailResponseDto {
    private Long id;
    private String title;
    private String content;
    private String category;
    private AuthorInfo author;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createdAt;
    private List<CommunityCommentResponseDto> comments;
    private int commentCount;

    @Data
    public static class AuthorInfo {
        private Long id;
        private String username;
    }
}

