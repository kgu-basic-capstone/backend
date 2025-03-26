package uk.jinhy.server.api.community.presentation;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

public class CommunityDto {

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CommunityPostRequest {
        private String title;
        private String content;
        private String category;
    }

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CommunityPostDetailResponse {
        private Long id;
        private String title;
        private String content;
        private String category;
        private AuthorInfo author;
        @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
        private LocalDateTime createdAt;
        private List<CommunityCommentResponse> comments;
        private int commentCount;

        @Getter
        @Builder
        @NoArgsConstructor
        @AllArgsConstructor
        public static class AuthorInfo {
            private Long id;
            private String username;
        }
    }

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CommunityPostListResponse {
        private List<CommunityPostDetailResponse> posts;
        private int total;
        private int page;
        private int size;
    }

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CommunityCommentRequest {
        private String content;
    }

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CommunityCommentResponse {
        private Long id;
        private String content;
        private AuthorInfo author;
        @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
        private LocalDateTime createdAt;

        @Getter
        @Builder
        @NoArgsConstructor
        @AllArgsConstructor
        public static class AuthorInfo {
            private Long id;
            private String username;
        }
    }
}
