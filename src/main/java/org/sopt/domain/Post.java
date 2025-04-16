package org.sopt.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import org.sopt.dto.request.post.PostRequest;

@Entity
public class Post {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String title;

    public Post(String title) {
        this.title = title;
    }

    public Post() {
    }

    public static Post from(PostRequest postRequest) {
        return new Post(postRequest.title());
    }

    public Long getId() {
        return this.id;
    }

    public String getTitle() {
        return this.title;
    }

    public Post updateTitle(String newTitle) {
        this.title = newTitle;
        return this;
    }
}
