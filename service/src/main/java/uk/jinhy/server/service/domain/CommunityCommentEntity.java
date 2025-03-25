package uk.jinhy.server.service.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "community_comments")
public class CommunityCommentEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id", nullable = false)
    private CommunityPostEntity post;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "author_id", nullable = false)
    private UserEntity author;

    @Column(length = 2000)
    private String content;

    @Column(nullable = false)
    private LocalDateTime createdAt;

    @Builder
    public CommunityCommentEntity(CommunityPostEntity post, UserEntity author, String content) {
        this.post = post;
        this.author = author;
        this.content = content;
        this.createdAt = LocalDateTime.now();
    }

    public void updateContent(String newContent) {
        this.content = newContent;
    }
}
