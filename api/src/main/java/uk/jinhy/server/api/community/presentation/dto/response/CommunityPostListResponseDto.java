package uk.jinhy.server.api.community.presentation.dto.response;

import lombok.*;

import java.util.List;

@Data
public class CommunityPostListResponseDto {
    private List<CommunityPostDetailResponseDto> posts;
    private int total;
    private int page;
    private int size;
}
