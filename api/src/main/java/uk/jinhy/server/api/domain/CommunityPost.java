package uk.jinhy.server.api.domain;

import lombok.Builder;
import lombok.Getter;
import uk.jinhy.server.api.user.domain.User;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
public class CommunityPost {
    private Long id;
    private User author;
    private Category category;
    private String title;
    private String content;
    private LocalDateTime createdAt;
    private List<CommunityComment> comments;

    @Builder
    private CommunityPost(User author, Category category,
                          String title, String content) {
        this.author = author;
        this.category = category;
        this.title = title;
        this.content = content;
        this.createdAt = LocalDateTime.now();
        this.comments = new ArrayList<>();
    }

    public CommunityComment addComment(CommunityComment comment) {
        this.comments.add(comment);
        return comment;
    }

    public void updateContent(String newContent) {
        this.content = newContent;
    }
}
