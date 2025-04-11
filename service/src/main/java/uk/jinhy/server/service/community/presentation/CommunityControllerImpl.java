package uk.jinhy.server.service.community.presentation;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import uk.jinhy.server.api.community.application.CommunityService;
import uk.jinhy.server.api.community.domain.CommentNotFoundException;
import uk.jinhy.server.api.community.domain.CommunityComment;
import uk.jinhy.server.api.community.domain.CommunityPost;
import uk.jinhy.server.api.community.domain.PostNotFoundException;
import uk.jinhy.server.api.community.presentation.CommunityController;
import uk.jinhy.server.api.community.presentation.dto.request.CommunityCommentRequestDto;
import uk.jinhy.server.api.community.presentation.dto.request.CommunityPostRequestDto;
import uk.jinhy.server.api.community.presentation.dto.response.CommunityCommentResponseDto;
import uk.jinhy.server.api.community.presentation.dto.response.CommunityPostDetailResponseDto;
import uk.jinhy.server.api.community.presentation.dto.response.CommunityPostListResponseDto;
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
    public ResponseEntity<CommunityPostDetailResponseDto> createPost(CommunityPostRequestDto request) {
        Long userId = getCurrentUserId();
        CommunityPost post = communityService.createPost(communityMapper.toCreatePostDto(request), userId);
        CommunityPostDetailResponseDto response = communityMapper.toPostDetailResponse(post);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @Override
    public ResponseEntity<CommunityPostDetailResponseDto> updatePost(Long postId, CommunityPostRequestDto request) {
        try {
            Long userId = getCurrentUserId();
            CommunityPost post = communityService.updatePost(postId, communityMapper.toUpdatePostDto(request), userId);
            CommunityPostDetailResponseDto response = communityMapper.toPostDetailResponse(post);
            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException e) {
            if (e.getMessage().contains("권한")) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
            }
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    @Override
    public ResponseEntity<Void> deletePost(Long postId) {
        try {
            Long userId = getCurrentUserId();
            communityService.deletePost(postId, userId);
            return ResponseEntity.noContent().build();
        } catch (IllegalArgumentException e) {
            if (e.getMessage().contains("권한")) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
            }
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    @Override
    public ResponseEntity<CommunityCommentResponseDto> addComment(Long postId, CommunityCommentRequestDto request) {
        try {
            Long userId = getCurrentUserId();
            CommunityComment comment = communityService.addComment(postId, communityMapper.toAddCommentDto(request), userId);
            CommunityCommentResponseDto response = communityMapper.toCommentResponse(comment);
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    @Override
    public ResponseEntity<CommunityCommentResponseDto> updateComment(Long commentId, CommunityCommentRequestDto request) {
        try {
            Long userId = getCurrentUserId();
            CommunityComment comment = communityService.updateComment(commentId, communityMapper.toUpdateCommentDto(request), userId);
            CommunityCommentResponseDto response = communityMapper.toCommentResponse(comment);
            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException e) {
            if (e.getMessage().contains("권한")) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
            }
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    @Override
    public ResponseEntity<Void> deleteComment(Long commentId) {
        try {
            Long userId = getCurrentUserId();
            communityService.deleteComment(commentId, userId);
            return ResponseEntity.noContent().build();
        } catch (IllegalArgumentException e) {
            if (e.getMessage().contains("권한")) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
            }
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    private Long getCurrentUserId() {
        return 1L;
    }
}
