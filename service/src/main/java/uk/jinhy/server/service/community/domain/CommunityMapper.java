package uk.jinhy.server.service.community.domain;

import org.mapstruct.*;
import uk.jinhy.server.api.community.domain.*;
import uk.jinhy.server.api.community.application.dto.AddCommentDto;
import uk.jinhy.server.api.community.application.dto.CreatePostDto;
import uk.jinhy.server.api.community.application.dto.UpdateCommentDto;
import uk.jinhy.server.api.community.application.dto.UpdatePostDto;
import uk.jinhy.server.api.community.presentation.dto.request.CommunityCommentRequestDto;
import uk.jinhy.server.api.community.presentation.dto.request.CommunityPostRequestDto;
import uk.jinhy.server.api.community.presentation.dto.response.CommunityCommentResponseDto;
import uk.jinhy.server.api.community.presentation.dto.response.CommunityPostDetailResponseDto;
import uk.jinhy.server.api.community.presentation.dto.response.CommunityPostListResponseDto;
import uk.jinhy.server.service.user.domain.UserEntity;

import java.util.List;

@Mapper(componentModel = "spring")
public interface CommunityMapper {
    CommunityPost toDomain(CommunityPostEntity entity);
    CommunityPostEntity toEntity(CommunityPost domain);
    CommunityComment toDomain(CommunityCommentEntity entity);

    @Mapping(target = "id", source = "comment.id")
    @Mapping(target = "content", source = "comment.content")
    @Mapping(target = "author", source = "comment.author")
    @Mapping(target = "post", source = "post")
    CommunityCommentEntity toEntity(CommunityComment comment, CommunityPost post);

    CommunityPostAuthor toPostAuthor(UserEntity userEntity);

    CommunityCommentAuthor toCommentAuthor(UserEntity userEntity);

    void updateEntity(@MappingTarget CommunityPostEntity entity, CommunityPost domain);

    void updateEntity(@MappingTarget CommunityCommentEntity entity, CommunityComment domain);

    @Mapping(target = "total", expression = "java(posts.size())")
    CommunityPostListResponseDto toPostListResponse(List<CommunityPost> posts, int page, int size);

    CommunityPostDetailResponseDto toPostDetailResponse(CommunityPost post);

    CreatePostDto toCreatePostDto(CommunityPostRequestDto request);

    UpdatePostDto toUpdatePostDto(CommunityPostRequestDto request);

    AddCommentDto toAddCommentDto(CommunityCommentRequestDto request);

    UpdateCommentDto toUpdateCommentDto(CommunityCommentRequestDto request);

    CommunityCommentResponseDto toCommentResponse(CommunityComment comment);
}
