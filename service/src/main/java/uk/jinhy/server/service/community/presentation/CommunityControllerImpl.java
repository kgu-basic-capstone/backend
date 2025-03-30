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
import uk.jinhy.server.api.community.presentation.CommunityDto.CommunityCommentRequest;
import uk.jinhy.server.api.community.presentation.CommunityDto.CommunityCommentResponse;
import uk.jinhy.server.api.community.presentation.CommunityDto.CommunityPostRequest;
import uk.jinhy.server.api.community.presentation.CommunityDto.CommunityPostDetailResponse;
import uk.jinhy.server.api.community.presentation.CommunityDto.CommunityPostListResponse;
import uk.jinhy.server.service.community.domain.CommunityMapper;

import java.util.List;


@RestController
@RequiredArgsConstructor
public class CommunityControllerImpl implements CommunityController {

    private final CommunityService communityService;
    private final CommunityMapper communityMapper;

    @Override
    public ResponseEntity<CommunityPostListResponse> getPosts(String category, String keyword, int page, int size) {
        try {
            List<CommunityPost> posts = communityService.getPosts(category, keyword, page, size);
            CommunityPostListResponse response = communityMapper.toPostListResponse(posts, page, size);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @Override
    public ResponseEntity<CommunityPostDetailResponse> getPost(Long postId) {
        try {
            CommunityPost post = communityService.getPost(postId);
            CommunityPostDetailResponse response = communityMapper.toPostDetailResponse(post);
            return ResponseEntity.ok(response);
        } catch (PostNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @Override
    public ResponseEntity<CommunityPostDetailResponse> createPost(CommunityPostRequest request) {
        try {
            Long userId = getCurrentUserId();
            CommunityPost post = communityService.createPost(request, userId);
            CommunityPostDetailResponse response = communityMapper.toPostDetailResponse(post);
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @Override
    public ResponseEntity<CommunityPostDetailResponse> updatePost(Long postId, CommunityPostRequest request) {
        try {
            Long userId = getCurrentUserId();
            CommunityPost post = communityService.updatePost(postId, request, userId);
            CommunityPostDetailResponse response = communityMapper.toPostDetailResponse(post);
            return ResponseEntity.ok(response);
        } catch (PostNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        } catch (IllegalArgumentException e) {
            if (e.getMessage().contains("권한")) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
            }
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @Override
    public ResponseEntity<Void> deletePost(Long postId) {
        try {
            Long userId = getCurrentUserId();
            communityService.deletePost(postId, userId);
            return ResponseEntity.noContent().build();
        } catch (PostNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        } catch (IllegalArgumentException e) {
            if (e.getMessage().contains("권한")) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
            }
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @Override
    public ResponseEntity<CommunityCommentResponse> addComment(Long postId, CommunityCommentRequest request) {
        try {
            Long userId = getCurrentUserId();
            CommunityComment comment = communityService.addComment(postId, request, userId);
            CommunityCommentResponse response = communityMapper.toCommentResponse(comment);
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (PostNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @Override
    public ResponseEntity<CommunityCommentResponse> updateComment(Long commentId, CommunityCommentRequest request) {
        try {
            Long userId = getCurrentUserId();
            CommunityComment comment = communityService.updateComment(commentId, request, userId);
            CommunityCommentResponse response = communityMapper.toCommentResponse(comment);
            return ResponseEntity.ok(response);
        } catch (CommentNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        } catch (IllegalArgumentException e) {
            if (e.getMessage().contains("권한")) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
            }
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @Override
    public ResponseEntity<Void> deleteComment(Long commentId) {
        try {
            Long userId = getCurrentUserId();
            communityService.deleteComment(commentId, userId);
            return ResponseEntity.noContent().build();
        } catch (CommentNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        } catch (IllegalArgumentException e) {
            if (e.getMessage().contains("권한")) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
            }
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    private Long getCurrentUserId() {
        return 1L;
    }
}
