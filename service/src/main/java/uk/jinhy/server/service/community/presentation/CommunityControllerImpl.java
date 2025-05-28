package uk.jinhy.server.service.community.presentation;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import uk.jinhy.server.api.community.application.CommunityService;
import uk.jinhy.server.api.community.domain.CommunityComment;
import uk.jinhy.server.api.community.domain.CommunityPost;
import uk.jinhy.server.api.community.presentation.CommunityController;
import uk.jinhy.server.api.community.presentation.dto.request.CommunityCommentRequestDto;
import uk.jinhy.server.api.community.presentation.dto.request.CommunityPostRequestDto;
import uk.jinhy.server.api.community.presentation.dto.response.CommunityCommentResponseDto;
import uk.jinhy.server.api.community.presentation.dto.response.CommunityPostDetailResponseDto;
import uk.jinhy.server.api.community.presentation.dto.response.CommunityPostListResponseDto;
import uk.jinhy.server.api.user.application.CurrentUser;
import uk.jinhy.server.api.user.domain.User;
import uk.jinhy.server.service.community.domain.CommunityMapper;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class CommunityControllerImpl implements CommunityController {
    private final CommunityService communityService;
    private final CommunityMapper communityMapper;

    @Override
    public ResponseEntity<CommunityPostListResponseDto> getPosts(String category, String keyword, int page, int size) {
        List<CommunityPost> posts = communityService.getPosts(category, keyword, page, size);
        CommunityPostListResponseDto response = communityMapper.toPostListResponse(posts, page, size);
        return ResponseEntity.ok(response);
    }

    @Override
    public ResponseEntity<CommunityPostDetailResponseDto> getPost(Long postId) {
        CommunityPost post = communityService.getPost(postId);
        CommunityPostDetailResponseDto response = communityMapper.toPostDetailResponse(post);
        return ResponseEntity.ok(response);
    }

    @Override
    public ResponseEntity<CommunityPostDetailResponseDto> createPost(CommunityPostRequestDto request, @CurrentUser User user) {
        CommunityPost post = communityService.createPost(communityMapper.toCreatePostDto(request), user);
        CommunityPostDetailResponseDto response = communityMapper.toPostDetailResponse(post);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @Override
    public ResponseEntity<CommunityPostDetailResponseDto> updatePost(Long postId, CommunityPostRequestDto request, @CurrentUser User user) {
        CommunityPost post = communityService.updatePost(postId, communityMapper.toUpdatePostDto(request), user);
        CommunityPostDetailResponseDto response = communityMapper.toPostDetailResponse(post);
        return ResponseEntity.ok(response);
    }

    @Override
    public ResponseEntity<Void> deletePost(Long postId, @CurrentUser User user) {
        communityService.deletePost(postId, user);
        return ResponseEntity.noContent().build();
    }

    @Override
    public ResponseEntity<CommunityCommentResponseDto> addComment(Long postId, CommunityCommentRequestDto request, @CurrentUser User user) {
        CommunityComment comment = communityService.addComment(postId, communityMapper.toAddCommentDto(request), user);
        CommunityCommentResponseDto response = communityMapper.toCommentResponse(comment);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @Override
    public ResponseEntity<CommunityCommentResponseDto> updateComment(Long commentId, CommunityCommentRequestDto request, @CurrentUser User user) {
        CommunityComment comment = communityService.updateComment(commentId, communityMapper.toUpdateCommentDto(request), user);
        CommunityCommentResponseDto response = communityMapper.toCommentResponse(comment);
        return ResponseEntity.ok(response);
    }

    @Override
    public ResponseEntity<Void> deleteComment(Long commentId, @CurrentUser User user) {
        communityService.deleteComment(commentId, user);
        return ResponseEntity.noContent().build();
    }
}
