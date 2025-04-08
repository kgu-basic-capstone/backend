package uk.jinhy.server.service.community.domain;

import jakarta.persistence.*;
import lombok.*;
import uk.jinhy.server.api.community.domain.Category;
import uk.jinhy.server.service.domain.UserEntity;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Table(name = "community_posts")
public class CommunityPostEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "author_id", nullable = false)
    private UserEntity author;

    private String title;
    private String content;

    @Enumerated(EnumType.STRING)
    private Category category;

    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<CommunityCommentEntity> comments = new ArrayList<>();
}
