package uk.jinhy.server.api.community.application;

import uk.jinhy.server.api.community.application.dto.AddCommentDto;
import uk.jinhy.server.api.community.application.dto.CreatePostDto;
import uk.jinhy.server.api.community.application.dto.UpdateCommentDto;
import uk.jinhy.server.api.community.application.dto.UpdatePostDto;
import uk.jinhy.server.api.community.domain.CommunityComment;
import uk.jinhy.server.api.community.domain.CommunityPost;

import java.util.List;

public interface CommunityService {
    List<CommunityPost> getPosts(String category, String keyword, int page, int size);
    CommunityPost getPost(Long postId);
    CommunityPost createPost(CreatePostDto request, Long userId);
    CommunityPost updatePost(Long postId, UpdatePostDto request, Long userId);
    void deletePost(Long postId, Long userId);
    CommunityComment addComment(Long postId, AddCommentDto request, Long userId);
    CommunityComment updateComment(Long commentId, UpdateCommentDto request, Long userId);
    void deleteComment(Long commentId, Long userId);
}
