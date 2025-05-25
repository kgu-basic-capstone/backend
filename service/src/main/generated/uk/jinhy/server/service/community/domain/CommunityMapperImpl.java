package uk.jinhy.server.service.community.domain;

import java.util.ArrayList;
import java.util.List;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;
import uk.jinhy.server.api.community.application.dto.AddCommentDto;
import uk.jinhy.server.api.community.application.dto.CreatePostDto;
import uk.jinhy.server.api.community.application.dto.UpdateCommentDto;
import uk.jinhy.server.api.community.application.dto.UpdatePostDto;
import uk.jinhy.server.api.community.domain.Category;
import uk.jinhy.server.api.community.domain.CommunityComment;
import uk.jinhy.server.api.community.domain.CommunityCommentAuthor;
import uk.jinhy.server.api.community.domain.CommunityPost;
import uk.jinhy.server.api.community.domain.CommunityPostAuthor;
import uk.jinhy.server.api.community.presentation.dto.request.CommunityCommentRequestDto;
import uk.jinhy.server.api.community.presentation.dto.request.CommunityPostRequestDto;
import uk.jinhy.server.api.community.presentation.dto.response.CommunityCommentResponseDto;
import uk.jinhy.server.api.community.presentation.dto.response.CommunityPostDetailResponseDto;
import uk.jinhy.server.api.community.presentation.dto.response.CommunityPostListResponseDto;
import uk.jinhy.server.service.user.domain.UserEntity;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-05-20T13:13:32+0000",
    comments = "version: 1.6.3, compiler: IncrementalProcessingEnvironment from gradle-language-java-8.5.jar, environment: Java 17.0.14 (Microsoft)"
)
@Component
public class CommunityMapperImpl implements CommunityMapper {

    @Override
    public CommunityPost toDomain(CommunityPostEntity entity) {
        if ( entity == null ) {
            return null;
        }

        CommunityPost.CommunityPostBuilder communityPost = CommunityPost.builder();

        communityPost.id( entity.getId() );
        communityPost.author( toPostAuthor( entity.getAuthor() ) );
        communityPost.category( entity.getCategory() );
        communityPost.title( entity.getTitle() );
        communityPost.content( entity.getContent() );
        communityPost.comments( communityCommentEntityListToCommunityCommentList( entity.getComments() ) );

        return communityPost.build();
    }

    @Override
    public CommunityPostEntity toEntity(CommunityPost domain) {
        if ( domain == null ) {
            return null;
        }

        Long id = null;
        UserEntity author = null;
        String title = null;
        String content = null;
        Category category = null;
        List<CommunityCommentEntity> comments = null;

        id = domain.getId();
        author = communityPostAuthorToUserEntity( domain.getAuthor() );
        title = domain.getTitle();
        content = domain.getContent();
        category = domain.getCategory();
        comments = communityCommentListToCommunityCommentEntityList( domain.getComments() );

        CommunityPostEntity communityPostEntity = new CommunityPostEntity( id, author, title, content, category, comments );

        return communityPostEntity;
    }

    @Override
    public CommunityComment toDomain(CommunityCommentEntity entity) {
        if ( entity == null ) {
            return null;
        }

        CommunityComment.CommunityCommentBuilder communityComment = CommunityComment.builder();

        communityComment.id( entity.getId() );
        communityComment.author( toCommentAuthor( entity.getAuthor() ) );
        communityComment.content( entity.getContent() );

        return communityComment.build();
    }

    @Override
    public CommunityCommentEntity toEntity(CommunityComment comment, CommunityPost post) {
        if ( comment == null && post == null ) {
            return null;
        }

        CommunityCommentEntity communityCommentEntity = new CommunityCommentEntity();

        if ( comment != null ) {
            communityCommentEntity.setId( comment.getId() );
            communityCommentEntity.setContent( comment.getContent() );
            communityCommentEntity.setAuthor( communityCommentAuthorToUserEntity( comment.getAuthor() ) );
        }
        communityCommentEntity.setPost( toEntity( post ) );

        return communityCommentEntity;
    }

    @Override
    public CommunityPostAuthor toPostAuthor(UserEntity userEntity) {
        if ( userEntity == null ) {
            return null;
        }

        CommunityPostAuthor.CommunityPostAuthorBuilder communityPostAuthor = CommunityPostAuthor.builder();

        communityPostAuthor.id( userEntity.getId() );
        communityPostAuthor.username( userEntity.getUsername() );

        return communityPostAuthor.build();
    }

    @Override
    public CommunityCommentAuthor toCommentAuthor(UserEntity userEntity) {
        if ( userEntity == null ) {
            return null;
        }

        CommunityCommentAuthor.CommunityCommentAuthorBuilder communityCommentAuthor = CommunityCommentAuthor.builder();

        communityCommentAuthor.id( userEntity.getId() );
        communityCommentAuthor.username( userEntity.getUsername() );

        return communityCommentAuthor.build();
    }

    @Override
    public void updateEntity(CommunityPostEntity entity, CommunityPost domain) {
        if ( domain == null ) {
            return;
        }

        entity.setId( domain.getId() );
        if ( domain.getAuthor() != null ) {
            if ( entity.getAuthor() == null ) {
                entity.setAuthor( UserEntity.builder().build() );
            }
            communityPostAuthorToUserEntity1( domain.getAuthor(), entity.getAuthor() );
        }
        else {
            entity.setAuthor( null );
        }
        entity.setTitle( domain.getTitle() );
        entity.setContent( domain.getContent() );
        entity.setCategory( domain.getCategory() );
        if ( entity.getComments() != null ) {
            List<CommunityCommentEntity> list = communityCommentListToCommunityCommentEntityList( domain.getComments() );
            if ( list != null ) {
                entity.getComments().clear();
                entity.getComments().addAll( list );
            }
            else {
                entity.setComments( null );
            }
        }
        else {
            List<CommunityCommentEntity> list = communityCommentListToCommunityCommentEntityList( domain.getComments() );
            if ( list != null ) {
                entity.setComments( list );
            }
        }
    }

    @Override
    public void updateEntity(CommunityCommentEntity entity, CommunityComment domain) {
        if ( domain == null ) {
            return;
        }

        entity.setId( domain.getId() );
        if ( domain.getAuthor() != null ) {
            if ( entity.getAuthor() == null ) {
                entity.setAuthor( UserEntity.builder().build() );
            }
            communityCommentAuthorToUserEntity1( domain.getAuthor(), entity.getAuthor() );
        }
        else {
            entity.setAuthor( null );
        }
        entity.setContent( domain.getContent() );
    }

    @Override
    public CommunityPostListResponseDto toPostListResponse(List<CommunityPost> posts, int page, int size) {
        if ( posts == null ) {
            return null;
        }

        CommunityPostListResponseDto communityPostListResponseDto = new CommunityPostListResponseDto();

        communityPostListResponseDto.setPosts( communityPostListToCommunityPostDetailResponseDtoList( posts ) );
        communityPostListResponseDto.setPage( page );
        communityPostListResponseDto.setSize( size );
        communityPostListResponseDto.setTotal( posts.size() );

        return communityPostListResponseDto;
    }

    @Override
    public CommunityPostDetailResponseDto toPostDetailResponse(CommunityPost post) {
        if ( post == null ) {
            return null;
        }

        CommunityPostDetailResponseDto communityPostDetailResponseDto = new CommunityPostDetailResponseDto();

        communityPostDetailResponseDto.setId( post.getId() );
        communityPostDetailResponseDto.setTitle( post.getTitle() );
        communityPostDetailResponseDto.setContent( post.getContent() );
        if ( post.getCategory() != null ) {
            communityPostDetailResponseDto.setCategory( post.getCategory().name() );
        }
        communityPostDetailResponseDto.setAuthor( communityPostAuthorToAuthorInfo( post.getAuthor() ) );
        communityPostDetailResponseDto.setComments( communityCommentListToCommunityCommentResponseDtoList( post.getComments() ) );

        return communityPostDetailResponseDto;
    }

    @Override
    public CreatePostDto toCreatePostDto(CommunityPostRequestDto request) {
        if ( request == null ) {
            return null;
        }

        CreatePostDto createPostDto = new CreatePostDto();

        createPostDto.setTitle( request.getTitle() );
        createPostDto.setContent( request.getContent() );
        createPostDto.setCategory( request.getCategory() );

        return createPostDto;
    }

    @Override
    public UpdatePostDto toUpdatePostDto(CommunityPostRequestDto request) {
        if ( request == null ) {
            return null;
        }

        UpdatePostDto updatePostDto = new UpdatePostDto();

        updatePostDto.setTitle( request.getTitle() );
        updatePostDto.setContent( request.getContent() );
        updatePostDto.setCategory( request.getCategory() );

        return updatePostDto;
    }

    @Override
    public AddCommentDto toAddCommentDto(CommunityCommentRequestDto request) {
        if ( request == null ) {
            return null;
        }

        AddCommentDto addCommentDto = new AddCommentDto();

        addCommentDto.setContent( request.getContent() );

        return addCommentDto;
    }

    @Override
    public UpdateCommentDto toUpdateCommentDto(CommunityCommentRequestDto request) {
        if ( request == null ) {
            return null;
        }

        UpdateCommentDto updateCommentDto = new UpdateCommentDto();

        updateCommentDto.setContent( request.getContent() );

        return updateCommentDto;
    }

    @Override
    public CommunityCommentResponseDto toCommentResponse(CommunityComment comment) {
        if ( comment == null ) {
            return null;
        }

        CommunityCommentResponseDto communityCommentResponseDto = new CommunityCommentResponseDto();

        communityCommentResponseDto.setId( comment.getId() );
        communityCommentResponseDto.setContent( comment.getContent() );
        communityCommentResponseDto.setAuthor( communityCommentAuthorToAuthorInfo( comment.getAuthor() ) );

        return communityCommentResponseDto;
    }

    protected List<CommunityComment> communityCommentEntityListToCommunityCommentList(List<CommunityCommentEntity> list) {
        if ( list == null ) {
            return null;
        }

        List<CommunityComment> list1 = new ArrayList<CommunityComment>( list.size() );
        for ( CommunityCommentEntity communityCommentEntity : list ) {
            list1.add( toDomain( communityCommentEntity ) );
        }

        return list1;
    }

    protected UserEntity communityPostAuthorToUserEntity(CommunityPostAuthor communityPostAuthor) {
        if ( communityPostAuthor == null ) {
            return null;
        }

        UserEntity.UserEntityBuilder userEntity = UserEntity.builder();

        userEntity.username( communityPostAuthor.getUsername() );

        return userEntity.build();
    }

    protected UserEntity communityCommentAuthorToUserEntity(CommunityCommentAuthor communityCommentAuthor) {
        if ( communityCommentAuthor == null ) {
            return null;
        }

        UserEntity.UserEntityBuilder userEntity = UserEntity.builder();

        userEntity.username( communityCommentAuthor.getUsername() );

        return userEntity.build();
    }

    protected CommunityCommentEntity communityCommentToCommunityCommentEntity(CommunityComment communityComment) {
        if ( communityComment == null ) {
            return null;
        }

        CommunityCommentEntity communityCommentEntity = new CommunityCommentEntity();

        communityCommentEntity.setId( communityComment.getId() );
        communityCommentEntity.setAuthor( communityCommentAuthorToUserEntity( communityComment.getAuthor() ) );
        communityCommentEntity.setContent( communityComment.getContent() );

        return communityCommentEntity;
    }

    protected List<CommunityCommentEntity> communityCommentListToCommunityCommentEntityList(List<CommunityComment> list) {
        if ( list == null ) {
            return null;
        }

        List<CommunityCommentEntity> list1 = new ArrayList<CommunityCommentEntity>( list.size() );
        for ( CommunityComment communityComment : list ) {
            list1.add( communityCommentToCommunityCommentEntity( communityComment ) );
        }

        return list1;
    }

    protected void communityPostAuthorToUserEntity1(CommunityPostAuthor communityPostAuthor, UserEntity mappingTarget) {
        if ( communityPostAuthor == null ) {
            return;
        }
    }

    protected void communityCommentAuthorToUserEntity1(CommunityCommentAuthor communityCommentAuthor, UserEntity mappingTarget) {
        if ( communityCommentAuthor == null ) {
            return;
        }
    }

    protected List<CommunityPostDetailResponseDto> communityPostListToCommunityPostDetailResponseDtoList(List<CommunityPost> list) {
        if ( list == null ) {
            return null;
        }

        List<CommunityPostDetailResponseDto> list1 = new ArrayList<CommunityPostDetailResponseDto>( list.size() );
        for ( CommunityPost communityPost : list ) {
            list1.add( toPostDetailResponse( communityPost ) );
        }

        return list1;
    }

    protected CommunityPostDetailResponseDto.AuthorInfo communityPostAuthorToAuthorInfo(CommunityPostAuthor communityPostAuthor) {
        if ( communityPostAuthor == null ) {
            return null;
        }

        CommunityPostDetailResponseDto.AuthorInfo authorInfo = new CommunityPostDetailResponseDto.AuthorInfo();

        authorInfo.setId( communityPostAuthor.getId() );
        authorInfo.setUsername( communityPostAuthor.getUsername() );

        return authorInfo;
    }

    protected List<CommunityCommentResponseDto> communityCommentListToCommunityCommentResponseDtoList(List<CommunityComment> list) {
        if ( list == null ) {
            return null;
        }

        List<CommunityCommentResponseDto> list1 = new ArrayList<CommunityCommentResponseDto>( list.size() );
        for ( CommunityComment communityComment : list ) {
            list1.add( toCommentResponse( communityComment ) );
        }

        return list1;
    }

    protected CommunityCommentResponseDto.AuthorInfo communityCommentAuthorToAuthorInfo(CommunityCommentAuthor communityCommentAuthor) {
        if ( communityCommentAuthor == null ) {
            return null;
        }

        CommunityCommentResponseDto.AuthorInfo authorInfo = new CommunityCommentResponseDto.AuthorInfo();

        authorInfo.setId( communityCommentAuthor.getId() );
        authorInfo.setUsername( communityCommentAuthor.getUsername() );

        return authorInfo;
    }
}
