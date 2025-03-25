package uk.jinhy.server.service.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import uk.jinhy.server.api.domain.Category;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "community_posts")
public class CommunityPostEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "author_id", nullable = false)
    private UserEntity author;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Category category;

    @Column(nullable = false)
    private String title;

    @Column(length = 5000)
    private String content;

    @Column(nullable = false)
    private LocalDateTime createdAt;

    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<CommunityCommentEntity> comments = new ArrayList<>();

    @Builder
    public CommunityPostEntity(UserEntity author, Category category,
                               String title, String content) {
        this.author = author;
        this.category = category;
        this.title = title;
        this.content = content;
        this.createdAt = LocalDateTime.now();
    }

    public CommunityCommentEntity addComment(CommunityCommentEntity comment) {
        this.comments.add(comment);
        return comment;
    }

    public void updateContent(String newContent) {
        this.content = newContent;
    }
}
