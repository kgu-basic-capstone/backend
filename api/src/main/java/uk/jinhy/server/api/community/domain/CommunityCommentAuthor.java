package uk.jinhy.server.api.community.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CommunityCommentAuthor {
    private Long id;
    private String username;
}
