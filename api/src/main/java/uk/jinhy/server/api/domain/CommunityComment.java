package uk.jinhy.server.api.domain;

import lombok.Builder;
import lombok.Getter;
import uk.jinhy.server.api.user.domain.User;

import java.time.LocalDateTime;

@Getter
public class CommunityComment {
    private Long id;
    private CommunityPost post;
    private User author;
    private String content;
    private LocalDateTime createdAt;

    @Builder
    private CommunityComment(CommunityPost post, User author, String content) {
        this.post = post;
        this.author = author;
        this.content = content;
        this.createdAt = LocalDateTime.now();
    }

    public void updateContent(String newContent) {
        this.content = newContent;
    }
}
