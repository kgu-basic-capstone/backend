package uk.jinhy.server.api.community.application.dto;

import lombok.*;

@Data
public class CreatePostDto {
    private String title;
    private String content;
    private String category;
}
