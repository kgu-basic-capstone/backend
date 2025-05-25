package uk.jinhy.server.api.community.presentation.dto.request;

import lombok.*;

@Data
public class CommunityPostRequestDto {
    private String title;
    private String content;
    private String category;
}
