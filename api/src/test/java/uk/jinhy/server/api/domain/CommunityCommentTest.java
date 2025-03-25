package uk.jinhy.server.api.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class CommunityCommentTest {
    @DisplayName("댓글 내용을 수정할 수 있다.")
    @Test
    void 댓글_내용_수정() {
        // given
        CommunityPost post = CommunityPost.builder()
            .author(UserFactory.create().build())
            .category(Category.QUESTIONS)
            .title("테스트 게시글")
            .content("테스트 내용")
            .build();

        CommunityComment sut = CommunityComment.builder()
            .post(post)
            .author(UserFactory.create().build())
            .content("기존 댓글")
            .build();

        // when
        sut.updateContent("수정된 댓글");

        // then
        assertThat(sut.getContent()).isEqualTo("수정된 댓글");
    }
}
