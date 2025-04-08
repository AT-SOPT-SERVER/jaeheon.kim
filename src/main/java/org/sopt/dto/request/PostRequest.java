package org.sopt.dto.request;

public class PostRequest {
    private final String title;

    public PostRequest(String title) {
        this.title = title;
    }

    public String getTitle() {
        return title;
    }
}
