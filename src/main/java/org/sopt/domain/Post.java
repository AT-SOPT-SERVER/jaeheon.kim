package org.sopt.domain;

import org.sopt.dto.request.PostRequest;

public class Post {
    private Long id;
    private String title;

    public Post(Long id, String title){
        this.id = id;
        this.title = title;
    }

    public Post(PostRequest postRequest){
        this.title = postRequest.getTitle();
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return this.title;
    }

    public Post updateTitle(String newTitle){
        this.title = newTitle;
        return this;
    }
}
