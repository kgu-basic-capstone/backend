package uk.jinhy.server.api.temp.community;

import uk.jinhy.server.api.domain.User;

public class CommunityComment {

    private Long id;
    private CommunityPost post;
    private User author;
    private String content;

}
