package uk.jinhy.server.api.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class CommunityPostTest {
    @DisplayName("게시글에 댓글을 추가할 수 있다.")
    @Test
    void 게시글_댓글_추가() {
        // given
        CommunityPost sut = CommunityPost.builder()
            .author(UserFactory.create().build())
            .category(Category.QUESTIONS)
            .title("테스트 게시글")
            .content("테스트 내용")
            .build();

        CommunityComment comment = CommunityComment.builder()
            .post(sut)
            .author(UserFactory.create().build())
            .content("테스트 댓글")
            .build();

        // when
        CommunityComment result = sut.addComment(comment);

        // then
        assertThat(result).isEqualTo(comment);
        assertThat(sut.getComments()).contains(comment);
    }

    @DisplayName("게시글 내용을 수정할 수 있다.")
    @Test
    void 게시글_내용_수정() {
        // given
        CommunityPost sut = CommunityPost.builder()
            .author(UserFactory.create().build())
            .category(Category.QUESTIONS)
            .title("테스트 게시글")
            .content("기존 내용")
            .build();

        // when
        sut.updateContent("수정된 내용");

        // then
        assertThat(sut.getContent()).isEqualTo("수정된 내용");
    }
}
