package org.sopt.service.post;

import org.sopt.domain.Post;
import org.sopt.dto.request.post.PostRequest;
import org.sopt.dto.request.post.PostUpdateRequest;
import org.sopt.dto.response.PostResponse;
import org.sopt.dto.response.PostResponses;
import org.sopt.exception.ConflictException;
import org.sopt.exception.errorcode.ErrorCode;
import org.springframework.stereotype.Service;

@Service
public class PostService {

    private final PostReader postReader;
    private final PostWriter postWriter;

    public PostService(final PostWriter postWriter,
                       final PostReader postReader) {
        this.postWriter = postWriter;
        this.postReader = postReader;
    }

    public PostResponse getPostById(final Long id) {
        return postReader.getPost(id);
    }

    public PostResponses getPosts(final String keyword) {
        return postReader.getPosts(keyword);
    }

    public void createPost(final PostRequest postRequest) {
        validDuplicatedTitle(postRequest.title());
        Post post = new Post(postRequest.title());
        postWriter.create(post);
    }

    public void deletePostById(final Long id) {
        Post post = postReader.findById(id);
        postWriter.delete(post);
    }

    public void updatePostById(final Long id, final PostUpdateRequest request) {
        Post post = postReader.findById(id);
        if (request.title().isPresent()) {
            String newTitle = request.title().get();
            validDuplicatedTitle(newTitle);
            postWriter.updateTitle(post, newTitle);
        }
    }

    private void validDuplicatedTitle(final String title) {
        if (postReader.isExistTitle(title)) {
            throw new ConflictException(ErrorCode.POST_TITLE_CONFLICT);
        }
    }

}
