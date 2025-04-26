package uk.jinhy.server.api.community.presentation;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import uk.jinhy.server.api.community.presentation.dto.request.CommunityCommentRequestDto;
import uk.jinhy.server.api.community.presentation.dto.request.CommunityPostRequestDto;
import uk.jinhy.server.api.community.presentation.dto.response.CommunityCommentResponseDto;
import uk.jinhy.server.api.community.presentation.dto.response.CommunityPostDetailResponseDto;
import uk.jinhy.server.api.community.presentation.dto.response.CommunityPostListResponseDto;

@Tag(name = "Community", description = "커뮤니티 API")
public interface CommunityController {

    @Operation(
        summary = "게시글 목록 조회",
        description = "커뮤니티 게시글 목록을 조회합니다. 카테고리별 필터링이 가능합니다.",
        responses = {
            @ApiResponse(responseCode = "200", description = "게시글 목록 조회 성공")
        }
    )
    @GetMapping("/api/community/posts")
    ResponseEntity<CommunityPostListResponseDto> getPosts(
        @Parameter(description = "카테고리 (HOSPITAL_REVIEW, SYMPTOMS, QUESTIONS, OTHER)")
        @RequestParam(required = false) String category,
        @Parameter(description = "검색 키워드") @RequestParam(required = false) String keyword,
        @Parameter(description = "페이지 번호") @RequestParam(required = false, defaultValue = "0") int page,
        @Parameter(description = "페이지 크기") @RequestParam(required = false, defaultValue = "20") int size
    );

    @Operation(
        summary = "게시글 상세 조회",
        description = "특정 게시글의 상세 정보를 조회합니다.",
        responses = {
            @ApiResponse(responseCode = "200", description = "게시글 조회 성공"),
            @ApiResponse(responseCode = "404", description = "게시글을 찾을 수 없음")
        }
    )
    @GetMapping("/api/community/posts/{postId}")
    ResponseEntity<CommunityPostDetailResponseDto> getPost(
        @Parameter(description = "게시글 ID") @PathVariable Long postId
    );

    @Operation(
        summary = "게시글 작성",
        description = "새로운 게시글을 작성합니다.",
        responses = {
            @ApiResponse(responseCode = "201", description = "게시글 작성 성공"),
            @ApiResponse(responseCode = "400", description = "잘못된 요청"),
            @ApiResponse(responseCode = "401", description = "인증 실패")
        }
    )
    @PostMapping("/api/community/posts")
    ResponseEntity<CommunityPostDetailResponseDto> createPost(
        @RequestBody CommunityPostRequestDto request
    );

    @Operation(
        summary = "게시글 수정",
        description = "기존 게시글을 수정합니다.",
        responses = {
            @ApiResponse(responseCode = "200", description = "게시글 수정 성공"),
            @ApiResponse(responseCode = "400", description = "잘못된 요청"),
            @ApiResponse(responseCode = "401", description = "인증 실패"),
            @ApiResponse(responseCode = "403", description = "권한 없음"),
            @ApiResponse(responseCode = "404", description = "게시글을 찾을 수 없음")
        }
    )
    @PutMapping("/api/community/posts/{postId}")
    ResponseEntity<CommunityPostDetailResponseDto> updatePost(
        @Parameter(description = "게시글 ID") @PathVariable Long postId,
        @RequestBody CommunityPostRequestDto request,
        @Parameter(description = "유저 ID") @RequestParam("userId") Long userId
    );

    @Operation(
        summary = "게시글 삭제",
        description = "게시글을 삭제합니다.",
        responses = {
            @ApiResponse(responseCode = "204", description = "게시글 삭제 성공"),
            @ApiResponse(responseCode = "401", description = "인증 실패"),
            @ApiResponse(responseCode = "403", description = "권한 없음"),
            @ApiResponse(responseCode = "404", description = "게시글을 찾을 수 없음")
        }
    )
    @DeleteMapping("/api/community/posts/{postId}")
    ResponseEntity<Void> deletePost(
        @Parameter(description = "게시글 ID") @PathVariable Long postId,
        @Parameter(description = "유저 ID") @RequestParam("userId") Long userId
    );

    @Operation(
        summary = "댓글 작성",
        description = "게시글에 새 댓글을 작성합니다.",
        responses = {
            @ApiResponse(responseCode = "201", description = "댓글 작성 성공"),
            @ApiResponse(responseCode = "400", description = "잘못된 요청"),
            @ApiResponse(responseCode = "401", description = "인증 실패"),
            @ApiResponse(responseCode = "404", description = "게시글을 찾을 수 없음")
        }
    )
    @PostMapping("/api/community/posts/{postId}/comments")
    ResponseEntity<CommunityCommentResponseDto> addComment(
        @Parameter(description = "게시글 ID") @PathVariable Long postId,
        @RequestBody CommunityCommentRequestDto request
    );

    @Operation(
        summary = "댓글 수정",
        description = "작성한 댓글을 수정합니다.",
        responses = {
            @ApiResponse(responseCode = "200", description = "댓글 수정 성공"),
            @ApiResponse(responseCode = "400", description = "잘못된 요청"),
            @ApiResponse(responseCode = "401", description = "인증 실패"),
            @ApiResponse(responseCode = "403", description = "권한 없음"),
            @ApiResponse(responseCode = "404", description = "댓글을 찾을 수 없음")
        }
    )
    @PutMapping("/api/community/comments/{commentId}")
    ResponseEntity<CommunityCommentResponseDto> updateComment(
        @Parameter(description = "댓글 ID") @PathVariable Long commentId,
        @RequestBody CommunityCommentRequestDto request,
        @Parameter(description = "유저 ID") @RequestParam("userId") Long userId
    );

    @Operation(
        summary = "댓글 삭제",
        description = "댓글을 삭제합니다.",
        responses = {
            @ApiResponse(responseCode = "204", description = "댓글 삭제 성공"),
            @ApiResponse(responseCode = "401", description = "인증 실패"),
            @ApiResponse(responseCode = "403", description = "권한 없음"),
            @ApiResponse(responseCode = "404", description = "댓글을 찾을 수 없음")
        }
    )
    @DeleteMapping("/api/community/comments/{commentId}")
    ResponseEntity<Void> deleteComment(
        @Parameter(description = "댓글 ID") @PathVariable Long commentId,
        @Parameter(description = "유저 ID") @RequestParam("userId") Long userId
    );
}
