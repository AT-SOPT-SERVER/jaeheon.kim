package org.sopt.domain;

import jakarta.persistence.*;
import org.sopt.domain.base.BaseEntity;
import org.sopt.domain.enums.Tag;

@Entity
@Table(
        indexes = {
                @Index(name = "uk_title", columnList = "title", unique = true)
        }
)

public class Post extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String title;

    @Column(length = 1000, nullable = false)
    private String content;

    @Enumerated(EnumType.STRING)
    private Tag tag;

    public Post(User user, String title, String content, Tag tag) {
        this.user = user;
        this.title = title;
        this.content = content;
        this.tag = tag;
    }

    protected Post() {
    }

    public Long getId() {
        return this.id;
    }

    public String getTitle() {
        return this.title;
    }

    public User getUser() {
        return user;
    }

    public String getContent() {
        return content;
    }

    public Tag getTag() {
        return tag;
    }

    public void updateTitle(String newTitle) {
        this.title = newTitle;
    }

    public void updateContent(String newContent) {
        this.content = content;
    }
}
