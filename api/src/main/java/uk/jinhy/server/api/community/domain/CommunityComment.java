package uk.jinhy.server.api.community.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import uk.jinhy.server.api.community.domain.exception.CommentDeleteForbiddenException;
import uk.jinhy.server.api.community.domain.exception.CommentUpdateForbiddenException;
import uk.jinhy.server.api.community.domain.exception.PostDeleteForbiddenException;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CommunityComment {
    private Long id;
    private CommunityCommentAuthor author;
    private String content;

    public void updateContent(CommunityCommentAuthor editor, String newContent) {
        if (!isAuthor(editor)) {
            throw new CommentUpdateForbiddenException("댓글 작성자가 아닙니다.");
        }
        this.content = newContent;
    }

    private boolean isAuthor(CommunityCommentAuthor user) {
        return this.author.getId().equals(user.getId());
    }

    public void deleteValidation(CommunityCommentAuthor user) {
        if (!isAuthor(user)) {
            throw new CommentDeleteForbiddenException("작성자만 삭제할 수 있습니다.");
        }
    }
}
