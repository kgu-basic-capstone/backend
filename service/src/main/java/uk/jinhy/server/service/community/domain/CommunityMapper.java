package uk.jinhy.server.service.community.domain;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import uk.jinhy.server.api.community.domain.CommunityComment;
import uk.jinhy.server.api.community.domain.CommunityPost;
import uk.jinhy.server.api.community.presentation.CommunityDto;
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
    CommunityDto.CommunityPostDetailResponse toPostDetailResponse(CommunityPost post);

    CommunityDto.CommunityCommentResponse toCommentResponse(CommunityComment comment);

    @Mapping(target = "total", expression = "java(posts.size())")
    CommunityDto.CommunityPostListResponse toPostListResponse(List<CommunityPost> posts, int page, int size);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "comments", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    CommunityPostEntity toEntity(CommunityPost post);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    CommunityCommentEntity toEntity(CommunityComment comment);

    void updateEntity(@MappingTarget CommunityPostEntity entity, CommunityPost post);

    void updateEntity(@MappingTarget CommunityCommentEntity entity, CommunityComment comment);
}
