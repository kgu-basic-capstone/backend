package uk.jinhy.server.api.temp.community;

import uk.jinhy.server.api.domain.User;

import java.util.List;

public class CommunityPost {

    private Long id;
    private User author;
    private Category category;
    private String title;
    private String content;
    private List<CommunityComment> comments;

}
