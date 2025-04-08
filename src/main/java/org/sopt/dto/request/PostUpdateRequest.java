package org.sopt.dto.request;

public class PostUpdateRequest {
    private final Long id;
    private String title;

    public PostUpdateRequest(Long id, String title){
        this.id = id;
        this.title = title;
    }

    public Long getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }
}
