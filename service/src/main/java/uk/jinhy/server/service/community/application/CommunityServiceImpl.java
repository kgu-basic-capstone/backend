package uk.jinhy.server.service.community.application;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import uk.jinhy.server.api.community.application.CommunityService;
import uk.jinhy.server.api.community.domain.*;
import uk.jinhy.server.api.domain.User;
import uk.jinhy.server.service.community.domain.*;
import uk.jinhy.server.service.domain.UserEntity;
import uk.jinhy.server.service.user.domain.UserRepository;
import uk.jinhy.server.api.community.presentation.CommunityDto.CommunityPostRequest;
import uk.jinhy.server.api.community.presentation.CommunityDto.CommunityCommentRequest;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CommunityServiceImpl implements CommunityService {

    private final CommunityPostRepository postRepository;
    private final CommunityCommentRepository commentRepository;
    private final UserRepository userRepository;
    private final CommunityMapper communityMapper;

    @Override
    public List<CommunityPost> getPosts(String categoryName, String keyword, int page, int size) {
        Category category = categoryName != null ? Category.valueOf(categoryName) : null;

        Page<CommunityPostEntity> postEntities = postRepository.findBySearchConditions(
            category,
            keyword,
            PageRequest.of(page, size));

        return postEntities.getContent().stream()
            .map(communityMapper::toDomain)
            .collect(Collectors.toList());
    }

    @Override
    public CommunityPost getPost(Long postId) {
        CommunityPostEntity postEntity = postRepository.findByIdWithAuthorAndComments(postId)
            .orElseThrow(() -> new PostNotFoundException("게시글을 찾을 수 없습니다: " + postId));

        return communityMapper.toDomain(postEntity);
    }

    @Override
    @Transactional
    public CommunityPost createPost(CommunityPostRequest request, Long userId) {
        UserEntity userEntity = userRepository.findById(userId)
            .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다: " + userId));

        User user = communityMapper.toDomain(userEntity);

        CommunityPost post = CommunityPost.builder()
            .title(request.getTitle())
            .content(request.getContent())
            .category(Category.valueOf(request.getCategory()))
            .author(user)
            .build();

        CommunityPostEntity postEntity = communityMapper.toEntity(post);
        CommunityPostEntity savedEntity = postRepository.save(postEntity);

        return communityMapper.toDomain(savedEntity);
    }

    @Override
    @Transactional
    public CommunityPost updatePost(Long postId, CommunityPostRequest request, Long userId) {
        CommunityPostEntity postEntity = postRepository.findByIdWithAuthor(postId)
            .orElseThrow(() -> new PostNotFoundException("게시글을 찾을 수 없습니다: " + postId));

        UserEntity userEntity = userRepository.findById(userId)
            .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다: " + userId));

        CommunityPost post = communityMapper.toDomain(postEntity);
        User user = communityMapper.toDomain(userEntity);

        if (!post.isAuthor(user)) {
            throw new IllegalArgumentException("게시글 수정 권한이 없습니다");
        }

        post.update(
            request.getTitle(),
            request.getContent(),
            Category.valueOf(request.getCategory()),
            user
        );

        communityMapper.updateEntity(postEntity, post);

        return communityMapper.toDomain(postEntity);
    }

    @Override
    @Transactional
    public void deletePost(Long postId, Long userId) {
        CommunityPostEntity postEntity = postRepository.findByIdWithAuthor(postId)
            .orElseThrow(() -> new PostNotFoundException("게시글을 찾을 수 없습니다: " + postId));

        UserEntity userEntity = userRepository.findById(userId)
            .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다: " + userId));

        CommunityPost post = communityMapper.toDomain(postEntity);
        User user = communityMapper.toDomain(userEntity);

        if (!post.isAuthor(user)) {
            throw new IllegalArgumentException("게시글 삭제 권한이 없습니다");
        }

        postRepository.delete(postEntity);
    }

    @Override
    @Transactional
    public CommunityComment addComment(Long postId, CommunityCommentRequest request, Long userId) {
        CommunityPostEntity postEntity = postRepository.findById(postId)
            .orElseThrow(() -> new PostNotFoundException("게시글을 찾을 수 없습니다: " + postId));

        UserEntity userEntity = userRepository.findById(userId)
            .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다: " + userId));

        CommunityPost post = communityMapper.toDomain(postEntity);
        User user = communityMapper.toDomain(userEntity);

        CommunityComment comment = CommunityComment.builder()
            .post(post)
            .author(user)
            .content(request.getContent())
            .build();

        post.addComment(comment);

        CommunityCommentEntity commentEntity = communityMapper.toEntity(comment);
        CommunityCommentEntity savedEntity = commentRepository.save(commentEntity);

        return communityMapper.toDomain(savedEntity);
    }

    @Override
    @Transactional
    public CommunityComment updateComment(Long commentId, CommunityCommentRequest request, Long userId) {
        CommunityCommentEntity commentEntity = commentRepository.findByIdWithAuthor(commentId)
            .orElseThrow(() -> new CommentNotFoundException("댓글을 찾을 수 없습니다: " + commentId));

        UserEntity userEntity = userRepository.findById(userId)
            .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다: " + userId));

        CommunityComment comment = communityMapper.toDomain(commentEntity);
        User user = communityMapper.toDomain(userEntity);

        if (!comment.isAuthor(user)) {
            throw new IllegalArgumentException("댓글 수정 권한이 없습니다");
        }

        comment.updateContent(request.getContent());

        communityMapper.updateEntity(commentEntity, comment);

        return communityMapper.toDomain(commentEntity);
    }

    @Override
    @Transactional
    public void deleteComment(Long commentId, Long userId) {
        CommunityCommentEntity commentEntity = commentRepository.findByIdWithAuthor(commentId)
            .orElseThrow(() -> new CommentNotFoundException("댓글을 찾을 수 없습니다: " + commentId));

        UserEntity userEntity = userRepository.findById(userId)
            .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다: " + userId));

        CommunityComment comment = communityMapper.toDomain(commentEntity);
        User user = communityMapper.toDomain(userEntity);

        if (!comment.isAuthor(user)) {
            throw new IllegalArgumentException("댓글 삭제 권한이 없습니다");
        }

        commentRepository.delete(commentEntity);
    }
}
