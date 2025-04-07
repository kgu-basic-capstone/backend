package uk.jinhy.server.service.community.application;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import uk.jinhy.server.api.community.application.dto.AddCommentDto;
import uk.jinhy.server.api.community.application.dto.CreatePostDto;
import uk.jinhy.server.api.community.application.dto.UpdateCommentDto;
import uk.jinhy.server.api.community.application.dto.UpdatePostDto;
import uk.jinhy.server.api.community.domain.*;
import uk.jinhy.server.api.domain.User;
import uk.jinhy.server.service.community.domain.*;
import uk.jinhy.server.service.domain.UserEntity;
import uk.jinhy.server.service.user.domain.UserRepository;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.*;

import org.mockito.InjectMocks;
import org.mockito.Mock;

@ExtendWith(MockitoExtension.class)
class CommunityServiceTest {

    @Mock
    private CommunityPostRepository postRepository;

    @Mock
    private CommunityCommentRepository commentRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private CommunityMapper communityMapper;

    @InjectMocks
    private CommunityServiceImpl communityService;

    @Nested
    @DisplayName("게시글 목록 조회 테스트")
    class GetPostsTest {

        @Test
        @DisplayName("카테고리와 키워드로 게시글 목록을 조회할 수 있다")
        void 카테고리와_키워드로_게시글_목록_조회() {
            // given
            String categoryName = "NOTICE";
            String keyword = "test";
            int page = 0;
            int size = 10;

            Category category = Category.NOTICE;

            CommunityPostEntity postEntity1 = new CommunityPostEntity();
            CommunityPostEntity postEntity2 = new CommunityPostEntity();
            List<CommunityPostEntity> postEntities = Arrays.asList(postEntity1, postEntity2);
            Page<CommunityPostEntity> postEntityPage = new PageImpl<>(postEntities);

            CommunityPost post1 = mock(CommunityPost.class);
            CommunityPost post2 = mock(CommunityPost.class);

            given(postRepository.findBySearchConditions(eq(category), eq(keyword), any(PageRequest.class)))
                .willReturn(postEntityPage);
            given(communityMapper.toDomain(postEntity1)).willReturn(post1);
            given(communityMapper.toDomain(postEntity2)).willReturn(post2);

            // when
            List<CommunityPost> result = communityService.getPosts(categoryName, keyword, page, size);

            // then
            assertThat(result).hasSize(2);
            assertThat(result).containsExactly(post1, post2);
        }

        @Test
        @DisplayName("카테고리 없이 키워드로만 게시글 목록을 조회할 수 있다")
        void 키워드로만_게시글_목록_조회() {
            // given
            String categoryName = null;
            String keyword = "test";
            int page = 0;
            int size = 10;

            CommunityPostEntity postEntity = new CommunityPostEntity();
            List<CommunityPostEntity> postEntities = Arrays.asList(postEntity);
            Page<CommunityPostEntity> postEntityPage = new PageImpl<>(postEntities);

            CommunityPost post = mock(CommunityPost.class);

            given(postRepository.findBySearchConditions(isNull(), eq(keyword), any(PageRequest.class)))
                .willReturn(postEntityPage);
            given(communityMapper.toDomain(postEntity)).willReturn(post);

            // when
            List<CommunityPost> result = communityService.getPosts(categoryName, keyword, page, size);

            // then
            assertThat(result).hasSize(1);
            assertThat(result).containsExactly(post);
        }
    }

    @Nested
    @DisplayName("단일 게시글 조회 테스트")
    class GetPostTest {

        @Test
        @DisplayName("ID로 게시글을 조회할 수 있다")
        void ID로_게시글_조회() {
            // given
            Long postId = 1L;
            CommunityPostEntity postEntity = new CommunityPostEntity();
            CommunityPost post = mock(CommunityPost.class);

            given(postRepository.findByIdWithAuthorAndComments(postId)).willReturn(Optional.of(postEntity));
            given(communityMapper.toDomain(postEntity)).willReturn(post);

            // when
            CommunityPost result = communityService.getPost(postId);

            // then
            assertThat(result).isEqualTo(post);
        }

        @Test
        @DisplayName("존재하지 않는 ID로 게시글 조회 시 예외가 발생한다")
        void 존재하지_않는_게시글_조회시_예외발생() {
            // given
            Long postId = 999L;
            given(postRepository.findByIdWithAuthorAndComments(postId)).willReturn(Optional.empty());

            // when & then
            assertThatExceptionOfType(PostNotFoundException.class)
                .isThrownBy(() -> communityService.getPost(postId));
        }
    }

    @Nested
    @DisplayName("게시글 생성 테스트")
    class CreatePostTest {

        @Test
        @DisplayName("사용자는 게시글을 작성할 수 있다")
        void 사용자_게시글_작성() {
            // given
            Long userId = 1L;

            CreatePostDto request = new CreatePostDto();
            request.setTitle("Test Title");
            request.setContent("Test Content");
            request.setCategory("NOTICE");

            UserEntity userEntity = new UserEntity();
            User user = mock(User.class);

            CommunityPost post = mock(CommunityPost.class);

            CommunityPostEntity postEntity = new CommunityPostEntity();
            CommunityPostEntity savedEntity = new CommunityPostEntity();

            given(userRepository.findById(userId)).willReturn(Optional.of(userEntity));
            given(communityMapper.toDomain(userEntity)).willReturn(user);
            given(communityMapper.toEntity(any(CommunityPost.class))).willReturn(postEntity);
            given(postRepository.save(postEntity)).willReturn(savedEntity);
            given(communityMapper.toDomain(savedEntity)).willReturn(post);

            // when
            CommunityPost result = communityService.createPost(request, userId);

            // then
            assertThat(result).isEqualTo(post);
        }

        @Test
        @DisplayName("존재하지 않는 사용자가 게시글 작성 시 예외가 발생한다")
        void 존재하지_않는_사용자_게시글_작성시_예외발생() {
            // given
            Long userId = 999L;
            CreatePostDto request = new CreatePostDto();

            given(userRepository.findById(userId)).willReturn(Optional.empty());

            // when & then
            assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> communityService.createPost(request, userId));
        }
    }

    @Nested
    @DisplayName("게시글 수정 테스트")
    class UpdatePostTest {
        @Test
        @DisplayName("작성자는 자신의 게시글을 수정할 수 있다")
        void 작성자_자신의_게시글_수정() {
            // given
            Long postId = 1L;
            Long userId = 1L;

            UpdatePostDto request = new UpdatePostDto();
            request.setTitle("Updated Title");
            request.setContent("Updated Content");
            request.setCategory("NOTICE");

            UserEntity userEntity = new UserEntity();
            User user = mock(User.class);

            CommunityPostEntity postEntity = new CommunityPostEntity();
            CommunityPost post = mock(CommunityPost.class);

            given(postRepository.findByIdWithAuthor(postId)).willReturn(Optional.of(postEntity));
            given(userRepository.findById(userId)).willReturn(Optional.of(userEntity));
            given(communityMapper.toDomain(postEntity)).willReturn(post);
            given(communityMapper.toDomain(userEntity)).willReturn(user);
            given(post.isAuthor(user)).willReturn(true);

            // when
            CommunityPost result = communityService.updatePost(postId, request, userId);

            // then
            assertThat(result).isEqualTo(post);
            then(communityMapper).should().updateEntity(eq(postEntity), eq(post));
            then(post).should().update(eq(request.getTitle()), eq(request.getContent()), any(Category.class), eq(user));
        }

        @Test
        @DisplayName("작성자가 아닌 사용자가 게시글 수정 시 예외가 발생한다")
        void 작성자가_아닌_사용자_게시글_수정시_예외발생() {
            // given
            Long postId = 1L;
            Long userId = 2L;

            UpdatePostDto request = new UpdatePostDto();

            UserEntity userEntity = new UserEntity();
            User user = mock(User.class);

            CommunityPostEntity postEntity = new CommunityPostEntity();
            CommunityPost post = mock(CommunityPost.class);

            given(post.isAuthor(user)).willReturn(false);
            given(postRepository.findByIdWithAuthor(postId)).willReturn(Optional.of(postEntity));
            given(userRepository.findById(userId)).willReturn(Optional.of(userEntity));
            given(communityMapper.toDomain(postEntity)).willReturn(post);
            given(communityMapper.toDomain(userEntity)).willReturn(user);

            // when & then
            assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> communityService.updatePost(postId, request, userId));

            // then
            then(communityMapper).should(never()).updateEntity(any(CommunityPostEntity.class), any(CommunityPost.class));
        }

        @Test
        @DisplayName("존재하지 않는 게시글 수정 시 예외가 발생한다")
        void 존재하지_않는_게시글_수정시_예외발생() {
            // given
            Long postId = 999L;
            Long userId = 1L;

            UpdatePostDto request = new UpdatePostDto();

            given(postRepository.findByIdWithAuthor(postId)).willReturn(Optional.empty());

            // when & then
            assertThatExceptionOfType(PostNotFoundException.class)
                .isThrownBy(() -> communityService.updatePost(postId, request, userId));
        }

        @Test
        @DisplayName("존재하지 않는 사용자가 게시글 수정 시 예외가 발생한다")
        void 존재하지_않는_사용자_게시글_수정시_예외발생() {
            // given
            Long postId = 1L;
            Long userId = 999L;

            UpdatePostDto request = new UpdatePostDto();

            CommunityPostEntity postEntity = new CommunityPostEntity();

            given(postRepository.findByIdWithAuthor(postId)).willReturn(Optional.of(postEntity));
            given(userRepository.findById(userId)).willReturn(Optional.empty());

            // when & then
            assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> communityService.updatePost(postId, request, userId));
        }
    }

    @Nested
    @DisplayName("게시글 삭제 테스트")
    class DeletePostTest {

        @Test
        @DisplayName("작성자는 자신의 게시글을 삭제할 수 있다")
        void 작성자_자신의_게시글_삭제() {
            // given
            Long postId = 1L;
            Long userId = 1L;

            UserEntity userEntity = new UserEntity();
            User user = mock(User.class);

            CommunityPostEntity postEntity = new CommunityPostEntity();
            CommunityPost post = mock(CommunityPost.class);

            given(post.isAuthor(user)).willReturn(true);
            given(postRepository.findByIdWithAuthor(postId)).willReturn(Optional.of(postEntity));
            given(userRepository.findById(userId)).willReturn(Optional.of(userEntity));
            given(communityMapper.toDomain(postEntity)).willReturn(post);
            given(communityMapper.toDomain(userEntity)).willReturn(user);

            // when
            communityService.deletePost(postId, userId);

            // then
            then(postRepository).should().delete(postEntity);
        }

        @Test
        @DisplayName("작성자가 아닌 사용자가 게시글 삭제 시 예외가 발생한다")
        void 작성자가_아닌_사용자_게시글_삭제시_예외발생() {
            // given
            Long postId = 1L;
            Long userId = 2L;

            UserEntity userEntity = new UserEntity();
            User user = mock(User.class);

            CommunityPostEntity postEntity = new CommunityPostEntity();
            CommunityPost post = mock(CommunityPost.class);

            given(post.isAuthor(user)).willReturn(false);
            given(postRepository.findByIdWithAuthor(postId)).willReturn(Optional.of(postEntity));
            given(userRepository.findById(userId)).willReturn(Optional.of(userEntity));
            given(communityMapper.toDomain(postEntity)).willReturn(post);
            given(communityMapper.toDomain(userEntity)).willReturn(user);

            // when & then
            assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> communityService.deletePost(postId, userId));

            // then
            then(postRepository).should(never()).delete(any());
        }

        @Test
        @DisplayName("존재하지 않는 게시글 삭제 시 예외가 발생한다")
        void 존재하지_않는_게시글_삭제시_예외발생() {
            // given
            Long postId = 999L;
            Long userId = 1L;

            given(postRepository.findByIdWithAuthor(postId)).willReturn(Optional.empty());

            // when & then
            assertThatExceptionOfType(PostNotFoundException.class)
                .isThrownBy(() -> communityService.deletePost(postId, userId));

            // then
            then(postRepository).should(never()).delete(any());
        }

        @Test
        @DisplayName("존재하지 않는 사용자가 게시글 삭제 시 예외가 발생한다")
        void 존재하지_않는_사용자_게시글_삭제시_예외발생() {
            // given
            Long postId = 1L;
            Long userId = 999L;

            CommunityPostEntity postEntity = new CommunityPostEntity();

            given(postRepository.findByIdWithAuthor(postId)).willReturn(Optional.of(postEntity));
            given(userRepository.findById(userId)).willReturn(Optional.empty());

            // when & then
            assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> communityService.deletePost(postId, userId));

            // then
            then(postRepository).should(never()).delete(any());
        }
    }

    @Nested
    @DisplayName("댓글 추가 테스트")
    class AddCommentTest {

        @Test
        @DisplayName("사용자는 게시글에 댓글을 작성할 수 있다")
        void 사용자_댓글_작성() {
            // given
            Long postId = 1L;
            Long userId = 1L;

            AddCommentDto request = new AddCommentDto();
            request.setContent("Test Comment");

            UserEntity userEntity = new UserEntity();
            User user = mock(User.class);

            CommunityPostEntity postEntity = new CommunityPostEntity();
            CommunityPost post = mock(CommunityPost.class);

            CommunityComment comment = mock(CommunityComment.class);

            CommunityCommentEntity commentEntity = new CommunityCommentEntity();
            CommunityCommentEntity savedEntity = new CommunityCommentEntity();

            given(postRepository.findById(postId)).willReturn(Optional.of(postEntity));
            given(userRepository.findById(userId)).willReturn(Optional.of(userEntity));
            given(communityMapper.toDomain(postEntity)).willReturn(post);
            given(communityMapper.toDomain(userEntity)).willReturn(user);
            given(communityMapper.toEntity(any(CommunityComment.class))).willReturn(commentEntity);
            given(commentRepository.save(commentEntity)).willReturn(savedEntity);
            given(communityMapper.toDomain(savedEntity)).willReturn(comment);

            // when
            CommunityComment result = communityService.addComment(postId, request, userId);

            // then
            assertThat(result).isEqualTo(comment);
            then(post).should().addComment(any(CommunityComment.class));
        }

        @Test
        @DisplayName("존재하지 않는 게시글에 댓글 작성 시 예외가 발생한다")
        void 존재하지_않는_게시글_댓글_작성시_예외발생() {
            // given
            Long postId = 999L;
            Long userId = 1L;

            AddCommentDto request = new AddCommentDto();

            given(postRepository.findById(postId)).willReturn(Optional.empty());

            // when & then
            assertThatExceptionOfType(PostNotFoundException.class)
                .isThrownBy(() -> communityService.addComment(postId, request, userId));

            // then
            then(commentRepository).should(never()).save(any());
        }

        @Test
        @DisplayName("존재하지 않는 사용자가 댓글 작성 시 예외가 발생한다")
        void 존재하지_않는_사용자_댓글_작성시_예외발생() {
            // given
            Long postId = 1L;
            Long userId = 999L;

            AddCommentDto request = new AddCommentDto();

            CommunityPostEntity postEntity = new CommunityPostEntity();

            given(postRepository.findById(postId)).willReturn(Optional.of(postEntity));
            given(userRepository.findById(userId)).willReturn(Optional.empty());

            // when & then
            assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> communityService.addComment(postId, request, userId));

            // then
            then(commentRepository).should(never()).save(any());
        }
    }

    @Nested
    @DisplayName("댓글 수정 테스트")
    class UpdateCommentTest {

        @Test
        @DisplayName("작성자는 자신의 댓글을 수정할 수 있다")
        void 작성자_자신의_댓글_수정() {
            // given
            Long commentId = 1L;
            Long userId = 1L;

            UpdateCommentDto request = new UpdateCommentDto();
            request.setContent("Updated Comment");

            UserEntity userEntity = new UserEntity();
            User user = mock(User.class);

            CommunityCommentEntity commentEntity = new CommunityCommentEntity();
            CommunityComment comment = mock(CommunityComment.class);

            given(commentRepository.findByIdWithAuthor(commentId)).willReturn(Optional.of(commentEntity));
            given(userRepository.findById(userId)).willReturn(Optional.of(userEntity));
            given(communityMapper.toDomain(commentEntity)).willReturn(comment);
            given(communityMapper.toDomain(userEntity)).willReturn(user);
            given(comment.isAuthor(user)).willReturn(true);

            // when
            CommunityComment result = communityService.updateComment(commentId, request, userId);

            // then
            assertThat(result).isEqualTo(comment);
            then(communityMapper).should().updateEntity(eq(commentEntity), eq(comment));
            then(comment).should().updateContent(eq(request.getContent()));
        }

        @Test
        @DisplayName("작성자가 아닌 사용자가 댓글 수정 시 예외가 발생한다")
        void 작성자가_아닌_사용자_댓글_수정시_예외발생() {
            // given
            Long commentId = 1L;
            Long userId = 2L;

            UpdateCommentDto request = new UpdateCommentDto();

            UserEntity userEntity = new UserEntity();
            User user = mock(User.class);

            CommunityCommentEntity commentEntity = new CommunityCommentEntity();
            CommunityComment comment = mock(CommunityComment.class);

            given(comment.isAuthor(user)).willReturn(false);
            given(commentRepository.findByIdWithAuthor(commentId)).willReturn(Optional.of(commentEntity));
            given(userRepository.findById(userId)).willReturn(Optional.of(userEntity));
            given(communityMapper.toDomain(commentEntity)).willReturn(comment);
            given(communityMapper.toDomain(userEntity)).willReturn(user);

            // when & then
            assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> communityService.updateComment(commentId, request, userId));

            // then
            then(communityMapper).should(never()).updateEntity(any(CommunityCommentEntity.class), any(CommunityComment.class));
        }

        @Test
        @DisplayName("존재하지 않는 댓글 수정 시 예외가 발생한다")
        void 존재하지_않는_댓글_수정시_예외발생() {
            // given
            Long commentId = 999L;
            Long userId = 1L;

            UpdateCommentDto request = new UpdateCommentDto();

            given(commentRepository.findByIdWithAuthor(commentId)).willReturn(Optional.empty());

            // when & then
            assertThatExceptionOfType(CommentNotFoundException.class)
                .isThrownBy(() -> communityService.updateComment(commentId, request, userId));

            // then
            then(communityMapper).should(never()).updateEntity(any(CommunityCommentEntity.class), any(CommunityComment.class));
        }

        @Test
        @DisplayName("존재하지 않는 사용자가 댓글 수정 시 예외가 발생한다")
        void 존재하지_않는_사용자_댓글_수정시_예외발생() {
            // given
            Long commentId = 1L;
            Long userId = 999L;

            UpdateCommentDto request = new UpdateCommentDto();

            CommunityCommentEntity commentEntity = new CommunityCommentEntity();

            given(commentRepository.findByIdWithAuthor(commentId)).willReturn(Optional.of(commentEntity));
            given(userRepository.findById(userId)).willReturn(Optional.empty());

            // when & then
            assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> communityService.updateComment(commentId, request, userId));

            // then
            then(communityMapper).should(never()).updateEntity(any(CommunityCommentEntity.class), any(CommunityComment.class));
        }
    }

    @Nested
    @DisplayName("댓글 삭제 테스트")
    class DeleteCommentTest {

        @Test
        @DisplayName("작성자는 자신의 댓글을 삭제할 수 있다")
        void 작성자_자신의_댓글_삭제() {
            // given
            Long commentId = 1L;
            Long userId = 1L;

            UserEntity userEntity = new UserEntity();
            User user = mock(User.class);

            CommunityCommentEntity commentEntity = new CommunityCommentEntity();
            CommunityComment comment = mock(CommunityComment.class);

            given(comment.isAuthor(user)).willReturn(true);
            given(commentRepository.findByIdWithAuthor(commentId)).willReturn(Optional.of(commentEntity));
            given(userRepository.findById(userId)).willReturn(Optional.of(userEntity));
            given(communityMapper.toDomain(commentEntity)).willReturn(comment);
            given(communityMapper.toDomain(userEntity)).willReturn(user);

            // when
            communityService.deleteComment(commentId, userId);

            // then
            then(commentRepository).should().delete(commentEntity);
        }

        @Test
        @DisplayName("작성자가 아닌 사용자가 댓글 삭제 시 예외가 발생한다")
        void 작성자가_아닌_사용자_댓글_삭제시_예외발생() {
            // given
            Long commentId = 1L;
            Long userId = 2L;

            UserEntity userEntity = new UserEntity();
            User user = mock(User.class);

            CommunityCommentEntity commentEntity = new CommunityCommentEntity();
            CommunityComment comment = mock(CommunityComment.class);

            given(comment.isAuthor(user)).willReturn(false);
            given(commentRepository.findByIdWithAuthor(commentId)).willReturn(Optional.of(commentEntity));
            given(userRepository.findById(userId)).willReturn(Optional.of(userEntity));
            given(communityMapper.toDomain(commentEntity)).willReturn(comment);
            given(communityMapper.toDomain(userEntity)).willReturn(user);

            // when & then
            assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> communityService.deleteComment(commentId, userId));

            // then
            then(commentRepository).should(never()).delete(any());
        }

        @Test
        @DisplayName("존재하지 않는 댓글 삭제 시 예외가 발생한다")
        void 존재하지_않는_댓글_삭제시_예외발생() {
            // given
            Long commentId = 999L;
            Long userId = 1L;

            given(commentRepository.findByIdWithAuthor(commentId)).willReturn(Optional.empty());

            // when & then
            assertThatExceptionOfType(CommentNotFoundException.class)
                .isThrownBy(() -> communityService.deleteComment(commentId, userId));

            // then
            then(commentRepository).should(never()).delete(any());
        }

        @Test
        @DisplayName("존재하지 않는 사용자가 댓글 삭제 시 예외가 발생한다")
        void 존재하지_않는_사용자_댓글_삭제시_예외발생() {
            // given
            Long commentId = 1L;
            Long userId = 999L;

            CommunityCommentEntity commentEntity = new CommunityCommentEntity();

            given(commentRepository.findByIdWithAuthor(commentId)).willReturn(Optional.of(commentEntity));
            given(userRepository.findById(userId)).willReturn(Optional.empty());

            // when & then
            assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> communityService.deleteComment(commentId, userId));

            // then
            then(commentRepository).should(never()).delete(any());
        }
    }
}
