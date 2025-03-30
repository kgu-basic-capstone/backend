package uk.jinhy.server.api.community.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import uk.jinhy.server.api.domain.User;

import java.time.LocalDateTime;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CommunityComment {
    private Long id;
    private CommunityPost post;
    private User author;
    private String content;
    private LocalDateTime createdAt;

    public boolean isAuthor(User user) {
        return this.author.getId().equals(user.getId());
    }
    public void updateContent(String newContent) {
        this.content = newContent;
    }
}
