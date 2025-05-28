package uk.jinhy.server.api.community.application;

import uk.jinhy.server.api.community.application.dto.AddCommentDto;
import uk.jinhy.server.api.community.application.dto.CreatePostDto;
import uk.jinhy.server.api.community.application.dto.UpdateCommentDto;
import uk.jinhy.server.api.community.application.dto.UpdatePostDto;
import uk.jinhy.server.api.community.domain.CommunityComment;
import uk.jinhy.server.api.community.domain.CommunityPost;
import uk.jinhy.server.api.user.domain.User;

import java.util.List;

public interface CommunityService {
    List<CommunityPost> getPosts(String category, String keyword, int page, int size);
    CommunityPost getPost(Long postId);
    CommunityPost createPost(CreatePostDto request, User user);
    CommunityPost updatePost(Long postId, UpdatePostDto request, User user);
    void deletePost(Long postId, User user);
    CommunityComment addComment(Long postId, AddCommentDto request, User user);
    CommunityComment updateComment(Long commentId, UpdateCommentDto request, User user);
    void deleteComment(Long commentId, User user);
}
