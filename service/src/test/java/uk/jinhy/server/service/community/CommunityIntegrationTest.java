package uk.jinhy.server.service.community;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.transaction.annotation.Transactional;
import uk.jinhy.server.api.community.application.CommunityService;
import uk.jinhy.server.api.community.application.dto.AddCommentDto;
import uk.jinhy.server.api.community.application.dto.CreatePostDto;
import uk.jinhy.server.api.community.domain.CommunityComment;
import uk.jinhy.server.api.community.domain.CommunityPost;
import uk.jinhy.server.api.community.presentation.dto.request.CommunityCommentRequestDto;
import uk.jinhy.server.api.community.presentation.dto.request.CommunityPostRequestDto;
import uk.jinhy.server.api.user.domain.User;
import uk.jinhy.server.service.common.IntegrationTest;

import com.fasterxml.jackson.databind.ObjectMapper;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.List;

@AutoConfigureMockMvc
@Transactional
class CommunityIntegrationTest extends IntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private CommunityService communityService;

    @PersistenceContext
    private EntityManager entityManager;

    private User testUser;
    private User otherUser;

    @Nested
    @DisplayName("게시글 목록 조회 테스트")
    class GetPostsTests {

        @BeforeEach
        void setUp() {
            testUser = createTestUser(1L, "testuser");

            CreatePostDto createPostDto = new CreatePostDto();
            createPostDto.setTitle("Test Post");
            createPostDto.setContent("Test Content");
            createPostDto.setCategory("NOTICE");

            communityService.createPost(createPostDto, testUser);
        }

        @Test
        @DisplayName("게시글 목록을 조회할 수 있다")
        void getPosts() throws Exception {
            // when
            ResultActions result = mockMvc.perform(get("/api/community/posts")
                .param("page", "0")
                .param("size", "10")
                .contentType(MediaType.APPLICATION_JSON));

            // then
            result.andExpect(status().isOk())
                .andExpect(jsonPath("$.posts", hasSize(greaterThanOrEqualTo(1))));
        }

        @Test
        @DisplayName("카테고리로 게시글 목록을 필터링할 수 있다")
        void getPostsByCategory() throws Exception {
            // when
            ResultActions result = mockMvc.perform(get("/api/community/posts")
                .param("category", "NOTICE")
                .param("page", "0")
                .param("size", "10")
                .contentType(MediaType.APPLICATION_JSON));

            // then
            result.andExpect(status().isOk())
                .andExpect(jsonPath("$.posts[0].category", is("NOTICE")));
        }

        @Test
        @DisplayName("키워드로 게시글 목록을 검색할 수 있다")
        void getPostsByKeyword() throws Exception {
            // when
            ResultActions result = mockMvc.perform(get("/api/community/posts")
                .param("keyword", "Test")
                .param("page", "0")
                .param("size", "10")
                .contentType(MediaType.APPLICATION_JSON));

            // then
            result.andExpect(status().isOk())
                .andExpect(jsonPath("$.posts", hasSize(greaterThanOrEqualTo(1))));
        }
    }

    @Nested
    @DisplayName("게시글 상세 조회 테스트")
    class GetPostTest {

        private Long postId;

        @BeforeEach
        void setUp() {
            testUser = createTestUser(1L, "testuser");

            // 서비스를 통한 게시글 생성
            CreatePostDto createPostDto = new CreatePostDto();
            createPostDto.setTitle("Test Post");
            createPostDto.setContent("Test Content");
            createPostDto.setCategory("NOTICE");

            CommunityPost post = communityService.createPost(createPostDto, testUser);
            postId = post.getId();
        }

        @Test
        @DisplayName("ID로 게시글을 조회할 수 있다")
        void getPostById() throws Exception {
            // when
            ResultActions result = mockMvc.perform(get("/api/community/posts/{postId}", postId)
                .contentType(MediaType.APPLICATION_JSON));

            // then
            result.andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(postId.intValue())))
                .andExpect(jsonPath("$.title", is("Test Post")))
                .andExpect(jsonPath("$.content", is("Test Content")))
                .andExpect(jsonPath("$.category", is("NOTICE")))
                .andExpect(jsonPath("$.author.username", is("testuser")));
        }

        @Test
        @DisplayName("존재하지 않는 게시글 조회 시 404 응답을 반환한다")
        void getNotExistingPost() throws Exception {
            // when
            ResultActions result = mockMvc.perform(get("/api/community/posts/9999")
                .contentType(MediaType.APPLICATION_JSON));

            // then
            result.andExpect(status().isNotFound());
        }
    }

    @Nested
    @DisplayName("게시글 생성 테스트")
    class CreatePostTest {

        @BeforeEach
        void setUp() {
            // 테스트 사용자 생성
            testUser = createTestUser(1L, "testuser");
        }

        @Test
        @DisplayName("새 게시글을 작성할 수 있다")
        void createPost() throws Exception {
            // given
            CommunityPostRequestDto requestDto = new CommunityPostRequestDto();
            requestDto.setTitle("New Post");
            requestDto.setContent("New Content");
            requestDto.setCategory("QNA");

            // when
            ResultActions result = mockMvc.perform(post("/api/community/posts")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(requestDto)));

            // then
            result.andExpect(status().isCreated())
                .andExpect(jsonPath("$.title", is("New Post")))
                .andExpect(jsonPath("$.content", is("New Content")))
                .andExpect(jsonPath("$.category", is("QNA")));
        }
    }

    @Nested
    @DisplayName("게시글 수정 테스트")
    class UpdatePostTest {

        private Long postId;

        @BeforeEach
        void setUp() {
            // 테스트 사용자 생성
            testUser = createTestUser(1L, "testuser");
            otherUser = createTestUser(2L, "otheruser");

            // 서비스를 통한 게시글 생성
            CreatePostDto createPostDto = new CreatePostDto();
            createPostDto.setTitle("Test Post");
            createPostDto.setContent("Test Content");
            createPostDto.setCategory("NOTICE");

            CommunityPost post = communityService.createPost(createPostDto, testUser);
            postId = post.getId();
        }

        @Test
        @DisplayName("게시글을 수정할 수 있다")
        void updatePost() throws Exception {
            // given
            CommunityPostRequestDto requestDto = new CommunityPostRequestDto();
            requestDto.setTitle("Updated Title");
            requestDto.setContent("Updated Content");
            requestDto.setCategory("QNA");

            // when
            ResultActions result = mockMvc.perform(put("/api/community/posts/{postId}", postId)
                .header("X-USER-ID", testUser.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(requestDto)));

            // then
            result.andExpect(status().isOk())
                .andExpect(jsonPath("$.title", is("Updated Title")))
                .andExpect(jsonPath("$.content", is("Updated Content")))
                .andExpect(jsonPath("$.category", is("QNA")));
        }

        @Test
        @DisplayName("다른 사용자의 게시글 수정 시 403 응답을 반환한다")
        void updatePostByOtherUser() throws Exception {
            // given
            CommunityPostRequestDto requestDto = new CommunityPostRequestDto();
            requestDto.setTitle("Updated by Other User");
            requestDto.setContent("This should not work");
            requestDto.setCategory("NOTICE");

            // when
            ResultActions result = mockMvc.perform(put("/api/community/posts/{postId}", postId)
                .header("X-USER-ID", otherUser.getId()) 
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(requestDto)));

            // then
            result.andExpect(status().is(anyOf(is(403), is(401))));
        }
    }

    @Nested
    @DisplayName("게시글 삭제 테스트")
    class DeletePostTest {

        private Long postId;

        @BeforeEach
        void setUp() {
            // 테스트 사용자 생성
            testUser = createTestUser(1L, "testuser");
            otherUser = createTestUser(2L, "otheruser");

            // 서비스를 통한 게시글 생성
            CreatePostDto createPostDto = new CreatePostDto();
            createPostDto.setTitle("Test Post");
            createPostDto.setContent("Test Content");
            createPostDto.setCategory("NOTICE");

            CommunityPost post = communityService.createPost(createPostDto, testUser);
            postId = post.getId();
        }

        @Test
        @DisplayName("게시글을 삭제할 수 있다")
        void deletePost() throws Exception {
            // when
            ResultActions result = mockMvc.perform(delete("/api/community/posts/{postId}", postId)
                .header("X-USER-ID", testUser.getId()));

            // then
            result.andExpect(status().isNoContent());

            // 삭제 확인
            mockMvc.perform(get("/api/community/posts/{postId}", postId))
                .andExpect(status().isNotFound());
        }

        @Test
        @DisplayName("다른 사용자의 게시글 삭제 시 403 응답을 반환한다")
        void deletePostByOtherUser() throws Exception {
            // when
            ResultActions result = mockMvc.perform(delete("/api/community/posts/{postId}", postId)
                .header("X-USER-ID", otherUser.getId()));

            // then
            result.andExpect(status().is(anyOf(is(403), is(401)))); // 권한 없음 또는 인증 실패
        }
    }

    @Nested
    @DisplayName("댓글 관련 테스트")
    class CommentTests {

        private Long postId;
        private Long commentId;

        @BeforeEach
        void setUp() {
            // 테스트 사용자 생성
            testUser = createTestUser(1L, "testuser");
            otherUser = createTestUser(2L, "otheruser");

            // 서비스를 통한 게시글 생성
            CreatePostDto createPostDto = new CreatePostDto();
            createPostDto.setTitle("Test Post");
            createPostDto.setContent("Test Content");
            createPostDto.setCategory("NOTICE");

            CommunityPost post = communityService.createPost(createPostDto, testUser);
            postId = post.getId();

            // 서비스를 통한 댓글 생성
            AddCommentDto addCommentDto = new AddCommentDto();
            addCommentDto.setContent("Test Comment");

            CommunityComment comment = communityService.addComment(postId, addCommentDto, testUser);
            commentId = comment.getId();
        }

        @Test
        @DisplayName("게시글에 댓글을 추가할 수 있다")
        void addComment() throws Exception {
            // given
            CommunityCommentRequestDto requestDto = new CommunityCommentRequestDto();
            requestDto.setContent("New Comment");

            // when
            ResultActions result = mockMvc.perform(post("/api/community/posts/{postId}/comments", postId)
                .header("X-USER-ID", testUser.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(requestDto)));

            // then
            result.andExpect(status().isCreated())
                .andExpect(jsonPath("$.content", is("New Comment")));
        }

        @Test
        @DisplayName("댓글을 수정할 수 있다")
        void updateComment() throws Exception {
            // given
            CommunityCommentRequestDto requestDto = new CommunityCommentRequestDto();
            requestDto.setContent("Updated Comment");

            // when
            ResultActions result = mockMvc.perform(put("/api/community/comments/{commentId}", commentId)
                .header("X-USER-ID", testUser.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(requestDto)));

            // then
            result.andExpect(status().isOk())
                .andExpect(jsonPath("$.content", is("Updated Comment")));
        }

        @Test
        @DisplayName("다른 사용자의 댓글 수정 시 403 응답을 반환한다")
        void updateCommentByOtherUser() throws Exception {
            // given
            CommunityCommentRequestDto requestDto = new CommunityCommentRequestDto();
            requestDto.setContent("Updated by Other User");

            // when
            ResultActions result = mockMvc.perform(put("/api/community/comments/{commentId}", commentId)
                .header("X-USER-ID", otherUser.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(requestDto)));

            // then
            result.andExpect(status().is(anyOf(is(403), is(401)))); // 권한 없음 또는 인증 실패
        }

        @Test
        @DisplayName("댓글을 삭제할 수 있다")
        void deleteComment() throws Exception {
            // when
            ResultActions result = mockMvc.perform(delete("/api/community/comments/{commentId}", commentId)
                .header("X-USER-ID", testUser.getId()));

            // then
            result.andExpect(status().isNoContent());
        }

        @Test
        @DisplayName("다른 사용자의 댓글 삭제 시 403 응답을 반환한다")
        void deleteCommentByOtherUser() throws Exception {
            // when
            ResultActions result = mockMvc.perform(delete("/api/community/comments/{commentId}", commentId)
                .header("X-USER-ID", otherUser.getId()));

            // then
            result.andExpect(status().is(anyOf(is(403), is(401)))); // 권한 없음 또는 인증 실패
        }

        @Test
        @DisplayName("존재하지 않는 댓글 수정 시 404 응답을 반환한다")
        void updateNotExistingComment() throws Exception {
            // given
            CommunityCommentRequestDto requestDto = new CommunityCommentRequestDto();
            requestDto.setContent("Updated Comment");

            // when
            ResultActions result = mockMvc.perform(put("/api/community/comments/9999")
                .header("X-USER-ID", testUser.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(requestDto)));

            // then
            result.andExpect(status().isNotFound());
        }

        @Test
        @DisplayName("존재하지 않는 게시글에 댓글 작성 시 404 응답을 반환한다")
        void addCommentToNotExistingPost() throws Exception {
            // given
            CommunityCommentRequestDto requestDto = new CommunityCommentRequestDto();
            requestDto.setContent("Test Comment");

            // when
            ResultActions result = mockMvc.perform(post("/api/community/posts/9999/comments")
                .header("X-USER-ID", testUser.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(requestDto)));

            // then
            result.andExpect(status().isNotFound());
        }
    }

    /**
     * 테스트 사용자를 SQL로 생성하는 도우미 메소드
     */
    private User createTestUser(Long userId, String username) {
        // 기존 사용자가 있으면 삭제 (테스트 독립성을 위해)
        entityManager.createNativeQuery("DELETE FROM users WHERE id = ?")
            .setParameter(1, userId)
            .executeUpdate();

        String userInsertQuery = "INSERT INTO users (id, username, email) " +
            "VALUES (?, ?, ?)";
        entityManager.createNativeQuery(userInsertQuery)
            .setParameter(1, userId)
            .setParameter(2, username)
            .setParameter(3, username + "@example.com")
            .executeUpdate();

        entityManager.flush();
        entityManager.clear();
        
        // User 객체를 Builder 패턴으로 생성
        return User.builder()
            .id(userId)
            .username(username)
            .email(username + "@example.com")
            .pets(List.of()) // 빈 리스트 설정
            .build();
    }
}
