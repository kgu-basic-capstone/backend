package uk.jinhy.server.service.community.application;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import uk.jinhy.server.api.community.application.CommunityService;
import uk.jinhy.server.api.community.application.dto.AddCommentDto;
import uk.jinhy.server.api.community.application.dto.CreatePostDto;
import uk.jinhy.server.api.community.application.dto.UpdateCommentDto;
import uk.jinhy.server.api.community.application.dto.UpdatePostDto;
import uk.jinhy.server.api.community.domain.*;
import uk.jinhy.server.api.community.domain.exception.CommentNotFoundException;
import uk.jinhy.server.api.community.domain.exception.PostNotFoundException;
import uk.jinhy.server.service.community.domain.*;
import uk.jinhy.server.service.user.domain.UserEntity;
import uk.jinhy.server.service.user.domain.UserRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Slf4j
public class CommunityServiceImpl implements CommunityService {
    private final CommunityPostRepository postRepository;
    private final CommunityCommentRepository commentRepository;
    private final UserRepository userRepository;
    private final CommunityMapper communityMapper;

    @Override
    public List<CommunityPost> getPosts(String categoryName, String keyword, int page, int size) {
        Category category = categoryName != null ? Category.valueOf(categoryName) : null;
        Page<CommunityPostEntity> postEntities = postRepository.findBySearchConditions(category, keyword, PageRequest.of(page, size));
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
    public CommunityPost createPost(CreatePostDto request, Long userId) {
        UserEntity userEntity = userRepository.findById(userId)
            .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다: " + userId));
        CommunityPostAuthor author = communityMapper.toPostAuthor(userEntity);
        CommunityPost post = CommunityPost.builder()
            .title(request.getTitle())
            .content(request.getContent())
            .category(Category.valueOf(request.getCategory()))
            .author(author)
            .build();
        CommunityPostEntity postEntity = communityMapper.toEntity(post);
        CommunityPostEntity savedEntity = postRepository.save(postEntity);
        return communityMapper.toDomain(savedEntity);
    }

    @Override
    @Transactional
    public CommunityPost updatePost(Long postId, UpdatePostDto request, Long userId) {
        CommunityPostEntity postEntity = postRepository.findByIdWithAuthor(postId)
            .orElseThrow(() -> new PostNotFoundException("게시글을 찾을 수 없습니다: " + postId));
        UserEntity userEntity = userRepository.findById(userId)
            .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다: " + userId));
        CommunityPost post = communityMapper.toDomain(postEntity);
        CommunityPostAuthor author = communityMapper.toPostAuthor(userEntity);
        post.update(request.getTitle(), request.getContent(), Category.valueOf(request.getCategory()), author);
        communityMapper.updateEntity(postEntity, post);
        postRepository.save(postEntity);
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
        CommunityPostAuthor author = communityMapper.toPostAuthor(userEntity);
        post.deleteValidation(author);
        postRepository.delete(postEntity);
    }

    @Override
    @Transactional
    public CommunityComment addComment(Long postId, AddCommentDto request, Long userId) {
        CommunityPostEntity postEntity = postRepository.findById(postId)
            .orElseThrow(() -> new PostNotFoundException("게시글을 찾을 수 없습니다: " + postId));
        UserEntity userEntity = userRepository.findById(userId)
            .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다: " + userId));
        CommunityPost post = communityMapper.toDomain(postEntity);
        CommunityCommentAuthor author = communityMapper.toCommentAuthor(userEntity);
        CommunityComment comment = post.addComment(author, request.getContent());
        CommunityCommentEntity commentEntity = communityMapper.toEntity(comment, post);
        CommunityCommentEntity savedEntity = commentRepository.save(commentEntity);
        return communityMapper.toDomain(savedEntity);
    }

    @Override
    @Transactional
    public CommunityComment updateComment(Long commentId, UpdateCommentDto request, Long userId) {
        CommunityCommentEntity commentEntity = commentRepository.findById(commentId)
            .orElseThrow(() -> new CommentNotFoundException("댓글을 찾을 수 없습니다: " + commentId));
        UserEntity userEntity = userRepository.findById(userId)
            .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다: " + userId));
        CommunityComment comment = communityMapper.toDomain(commentEntity);
        CommunityCommentAuthor author = communityMapper.toCommentAuthor(userEntity);
        comment.updateContent(author, request.getContent());
        communityMapper.updateEntity(commentEntity, comment);
        commentRepository.save(commentEntity);
        return communityMapper.toDomain(commentEntity);
    }

    @Override
    @Transactional
    public void deleteComment(Long commentId, Long userId) {
        CommunityCommentEntity commentEntity = commentRepository.findById(commentId)
            .orElseThrow(() -> new CommentNotFoundException("댓글을 찾을 수 없습니다: " + commentId));
        UserEntity userEntity = userRepository.findById(userId)
            .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다: " + userId));
        CommunityComment comment = communityMapper.toDomain(commentEntity);
        CommunityCommentAuthor author = communityMapper.toCommentAuthor(userEntity);
        comment.deleteValidation(author);
        commentRepository.delete(commentEntity);
    }
}
