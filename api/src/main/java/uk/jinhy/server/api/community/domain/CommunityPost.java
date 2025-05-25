package uk.jinhy.server.api.community.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import uk.jinhy.server.api.community.domain.exception.PostDeleteForbiddenException;
import uk.jinhy.server.api.community.domain.exception.PostUpdateForbiddenException;

import java.util.ArrayList;
import java.util.List;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CommunityPost {
    private Long id;
    private CommunityPostAuthor author;
    private Category category;
    private String title;
    private String content;

    @Builder.Default
    private List<CommunityComment> comments = new ArrayList<>();

    public CommunityComment addComment(CommunityCommentAuthor author, String content) {
        CommunityComment newComment = CommunityComment.builder()
            .author(author)
            .content(content)
            .build();
        this.comments.add(newComment);
        return newComment;
    }

    public void update(String newTitle, String newContent, Category newCategory, CommunityPostAuthor requestUser) {
        if (!isAuthor(requestUser)) {
            throw new PostUpdateForbiddenException("게시글 수정 권한이 없습니다");
        }
        this.title = newTitle;
        this.content = newContent;
        this.category = newCategory;
    }

    private boolean isAuthor(CommunityPostAuthor user) {
        return this.author.getId().equals(user.getId());
    }

    public void deleteValidation(CommunityPostAuthor user) {
        if (!isAuthor(user)) {
            throw new PostDeleteForbiddenException("작성자만 삭제할 수 있습니다.");
        }
    }
}
