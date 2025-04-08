package uk.jinhy.server.api.community.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import uk.jinhy.server.api.domain.User;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CommunityPost {
    private Long id;
    private User author;
    private Category category;
    private String title;
    private String content;
    private LocalDateTime createdAt;
    private List<CommunityComment> comments;

    public boolean isAuthor(User user) {
        return this.author.getId().equals(user.getId());
    }

    public CommunityComment addComment(CommunityComment comment) {
        this.comments.add(comment);
        return comment;
    }

    public void updateContent(String newContent) {
        this.content = newContent;
    }

    public void update(String newTitle, String newContent, Category newCategory, User requestUser) {
        if (!isAuthor(requestUser)) {
            throw new IllegalArgumentException("게시글 수정 권한이 없습니다");
        }
        this.title = newTitle;
        this.content = newContent;
        this.category = newCategory;
    }
}
