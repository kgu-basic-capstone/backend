package uk.jinhy.server.service.community.domain;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import uk.jinhy.server.api.community.application.dto.AddCommentDto;
import uk.jinhy.server.api.community.application.dto.CreatePostDto;
import uk.jinhy.server.api.community.application.dto.UpdateCommentDto;
import uk.jinhy.server.api.community.application.dto.UpdatePostDto;
import uk.jinhy.server.api.community.domain.CommunityComment;
import uk.jinhy.server.api.community.domain.CommunityPost;
import uk.jinhy.server.api.community.presentation.dto.request.CommunityCommentRequestDto;
import uk.jinhy.server.api.community.presentation.dto.request.CommunityPostRequestDto;
import uk.jinhy.server.api.community.presentation.dto.response.CommunityCommentResponseDto;
import uk.jinhy.server.api.community.presentation.dto.response.CommunityPostDetailResponseDto;
import uk.jinhy.server.api.community.presentation.dto.response.CommunityPostListResponseDto;
import uk.jinhy.server.api.domain.User;
import uk.jinhy.server.service.domain.UserEntity;

import java.util.List;

@Mapper(componentModel = "spring")
public interface CommunityMapper {
    @Mapping(target = "comments", ignore = true)
    CommunityPost toDomain(CommunityPostEntity entity);

    @Mapping(target = "post", ignore = true)
    CommunityComment toDomain(CommunityCommentEntity entity);

    User toDomain(UserEntity entity);

    @Mapping(target = "category", expression = "java(post.getCategory().name())")
    @Mapping(target = "commentCount", expression = "java(post.getComments().size())")
    CommunityPostDetailResponseDto toPostDetailResponse(CommunityPost post);

    CommunityCommentResponseDto toCommentResponse(CommunityComment comment);

    @Mapping(target = "total", expression = "java(posts.size())")
    CommunityPostListResponseDto toPostListResponse(List<CommunityPost> posts, int page, int size);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "comments", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    CommunityPostEntity toEntity(CommunityPost post);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    CommunityCommentEntity toEntity(CommunityComment comment);

    void updateEntity(@MappingTarget CommunityPostEntity entity, CommunityPost post);

    void updateEntity(@MappingTarget CommunityCommentEntity entity, CommunityComment comment);

    CreatePostDto toCreatePostDto(CommunityPostRequestDto request);

    UpdatePostDto toUpdatePostDto(CommunityPostRequestDto request);

    AddCommentDto toAddCommentDto(CommunityCommentRequestDto request);

    UpdateCommentDto toUpdateCommentDto(CommunityCommentRequestDto request);
}
