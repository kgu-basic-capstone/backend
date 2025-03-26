package uk.jinhy.server.service.community.presentation;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import uk.jinhy.server.api.community.presentation.CommunityController;
import uk.jinhy.server.api.community.presentation.CommunityDto.CommunityCommentRequest;
import uk.jinhy.server.api.community.presentation.CommunityDto.CommunityCommentResponse;
import uk.jinhy.server.api.community.presentation.CommunityDto.CommunityPostRequest;
import uk.jinhy.server.api.community.presentation.CommunityDto.CommunityPostDetailResponse;
import uk.jinhy.server.api.community.presentation.CommunityDto.CommunityPostListResponse;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
public class CommunityControllerImpl implements CommunityController {

    @Override
    public ResponseEntity<CommunityPostListResponse> getPosts(String category, String keyword, int page, int size) {
        List<CommunityPostDetailResponse> mockPosts = Arrays.asList(
            CommunityPostDetailResponse.builder()
                .id(1L)
                .title("강남 24시 동물병원 후기")
                .content("응급상황에 정말 도움이 많이 되었어요.")
                .category("HOSPITAL_REVIEW")
                .author(CommunityPostDetailResponse.AuthorInfo.builder()
                    .id(1L)
                    .username("애견맘")
                    .build())
                .createdAt(LocalDateTime.now().minusDays(2))
                .commentCount(3)
                .build(),
            CommunityPostDetailResponse.builder()
                .id(2L)
                .title("강아지가 갑자기 구토를 해요")
                .content("3살 말티즈인데 오늘 아침부터 구토를 합니다. 어떻게 해야할까요?")
                .category("SYMPTOMS")
                .author(CommunityPostDetailResponse.AuthorInfo.builder()
                    .id(2L)
                    .username("멍멍이아빠")
                    .build())
                .createdAt(LocalDateTime.now().minusHours(5))
                .commentCount(7)
                .build(),
            CommunityPostDetailResponse.builder()
                .id(3L)
                .title("고양이 백신 질문이요")
                .content("첫 백신은 언제 맞추는게 좋을까요?")
                .category("QUESTIONS")
                .author(CommunityPostDetailResponse.AuthorInfo.builder()
                    .id(3L)
                    .username("냥이집사")
                    .build())
                .createdAt(LocalDateTime.now().minusDays(1))
                .commentCount(4)
                .build()
        );

        if (category != null && !category.isEmpty()) {
            mockPosts = mockPosts.stream()
                .filter(post -> post.getCategory().equals(category))
                .collect(Collectors.toList());
        }

        if (keyword != null && !keyword.isEmpty()) {
            mockPosts = mockPosts.stream()
                .filter(post ->
                    post.getTitle().contains(keyword) ||
                        post.getContent().contains(keyword))
                .collect(Collectors.toList());
        }

        CommunityPostListResponse response = CommunityPostListResponse.builder()
            .posts(mockPosts)
            .total(mockPosts.size())
            .page(page)
            .size(size)
            .build();

        return ResponseEntity.ok(response);
    }

    @Override
    public ResponseEntity<CommunityPostDetailResponse> getPost(Long postId) {
        List<CommunityCommentResponse> mockComments = Arrays.asList(
            CommunityCommentResponse.builder()
                .id(1L)
                .content("저도 그 병원 이용해봤는데 정말 좋았어요!")
                .author(CommunityCommentResponse.AuthorInfo.builder()
                    .id(2L)
                    .username("강아지아빠")
                    .build())
                .createdAt(LocalDateTime.now().minusDays(1))
                .build(),
            CommunityCommentResponse.builder()
                .id(2L)
                .content("24시간 영업하는게 정말 큰 장점인 것 같아요.")
                .author(CommunityCommentResponse.AuthorInfo.builder()
                    .id(3L)
                    .username("고양이맘")
                    .build())
                .createdAt(LocalDateTime.now().minusHours(12))
                .build()
        );

        CommunityPostDetailResponse mockResponse = CommunityPostDetailResponse.builder()
            .id(postId)
            .title("강남 24시 동물병원 후기")
            .content("응급상황에 정말 도움이 많이 되었어요. 의사 선생님들도 친절하시고 시설도 깨끗합니다.")
            .category("HOSPITAL_REVIEW")
            .author(CommunityPostDetailResponse.AuthorInfo.builder()
                .id(1L)
                .username("애견맘")
                .build())
            .createdAt(LocalDateTime.now().minusDays(2))
            .comments(mockComments)
            .commentCount(mockComments.size())
            .build();

        return ResponseEntity.ok(mockResponse);
    }

    @Override
    public ResponseEntity<CommunityPostDetailResponse> createPost(CommunityPostRequest request) {
        CommunityPostDetailResponse mockResponse = CommunityPostDetailResponse.builder()
            .id(4L)
            .title(request.getTitle())
            .content(request.getContent())
            .category(request.getCategory())
            .author(CommunityPostDetailResponse.AuthorInfo.builder()
                .id(1L)
                .username("testUser")
                .build())
            .createdAt(LocalDateTime.now())
            .comments(List.of())
            .commentCount(0)
            .build();

        return ResponseEntity.status(HttpStatus.CREATED).body(mockResponse);
    }

    @Override
    public ResponseEntity<CommunityPostDetailResponse> updatePost(Long postId, CommunityPostRequest request) {
        CommunityPostDetailResponse mockResponse = CommunityPostDetailResponse.builder()
            .id(postId)
            .title(request.getTitle())
            .content(request.getContent())
            .category(request.getCategory())
            .author(CommunityPostDetailResponse.AuthorInfo.builder()
                .id(1L)
                .username("testUser")
                .build())
            .createdAt(LocalDateTime.now().minusDays(1))
            .comments(List.of())
            .commentCount(0)
            .build();

        return ResponseEntity.ok(mockResponse);
    }

    @Override
    public ResponseEntity<Void> deletePost(Long postId) {
        return ResponseEntity.noContent().build();
    }

    @Override
    public ResponseEntity<CommunityCommentResponse> addComment(Long postId, CommunityCommentRequest request) {
        CommunityCommentResponse mockResponse = CommunityCommentResponse.builder()
            .id(3L)
            .content(request.getContent())
            .author(CommunityCommentResponse.AuthorInfo.builder()
                .id(1L)
                .username("testUser")
                .build())
            .createdAt(LocalDateTime.now())
            .build();

        return ResponseEntity.status(HttpStatus.CREATED).body(mockResponse);
    }

    @Override
    public ResponseEntity<CommunityCommentResponse> updateComment(Long commentId, CommunityCommentRequest request) {
        CommunityCommentResponse mockResponse = CommunityCommentResponse.builder()
            .id(commentId)
            .content(request.getContent())
            .author(CommunityCommentResponse.AuthorInfo.builder()
                .id(1L)
                .username("testUser")
                .build())
            .createdAt(LocalDateTime.now().minusDays(1))
            .build();

        return ResponseEntity.ok(mockResponse);
    }

    @Override
    public ResponseEntity<Void> deleteComment(Long commentId) {
        return ResponseEntity.noContent().build();
    }
}
