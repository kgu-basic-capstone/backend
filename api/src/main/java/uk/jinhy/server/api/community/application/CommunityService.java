package uk.jinhy.server.api.community.application;
import uk.jinhy.server.api.community.domain.CommunityComment;
import uk.jinhy.server.api.community.domain.CommunityPost;
import uk.jinhy.server.api.community.presentation.CommunityDto.CommunityPostRequest;
import uk.jinhy.server.api.community.presentation.CommunityDto.CommunityCommentRequest;

import java.util.List;

public interface CommunityService {
    List<CommunityPost> getPosts(String category, String keyword, int page, int size);
    CommunityPost getPost(Long postId);
    CommunityPost createPost(CommunityPostRequest request, Long userId);
    CommunityPost updatePost(Long postId, CommunityPostRequest request, Long userId);
    void deletePost(Long postId, Long userId);
    CommunityComment addComment(Long postId, CommunityCommentRequest request, Long userId);
    CommunityComment updateComment(Long commentId, CommunityCommentRequest request, Long userId);
    void deleteComment(Long commentId, Long userId);
}
