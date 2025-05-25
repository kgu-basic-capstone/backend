package uk.jinhy.server.api.community.application.dto;

import lombok.*;

@Data
public class UpdatePostDto {
    private String title;
    private String content;
    private String category;
}
