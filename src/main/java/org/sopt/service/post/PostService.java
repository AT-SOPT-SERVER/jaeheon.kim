package org.sopt.service.post;

import org.sopt.domain.Post;
import org.sopt.domain.User;
import org.sopt.dto.request.post.PostCreateRequest;
import org.sopt.dto.request.post.PostUpdateRequest;
import org.sopt.dto.response.post.PostResponse;
import org.sopt.dto.response.post.PostResponses;
import org.sopt.exception.ConflictException;
import org.sopt.exception.ForbiddenException;
import org.sopt.exception.errorcode.ErrorCode;
import org.sopt.service.user.UserReader;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

@Service
public class PostService {

    private final PostReader postReader;
    private final PostWriter postWriter;
    private final UserReader userReader;

    public PostService(final PostWriter postWriter,
                       final PostReader postReader,
                       final UserReader userReader) {
        this.postWriter = postWriter;
        this.postReader = postReader;
        this.userReader = userReader;
    }

    public PostResponse getPostById(final Long id) {
        return postReader.getPost(id);
    }

    public PostPreviewResponses getPosts(final Optional<String> keyword,
                                         final String target,
                                         final Optional<String> tag) {
        Optional<Tag> tagOptional = Optional.of(tag.map(Tag::resolveTag)).orElse(null);
        return postReader.getPosts(keyword, target, tagOptional);
    }

    public void createPost(final Long userId, final PostCreateRequest postCreateRequest) {
        User user = userReader.findById(userId);
        Post post = new Post(user,
                postCreateRequest.title(),
                postCreateRequest.content(),
                Tag.resolveTag(postCreateRequest.tag()));
        postIntegrityRunnable(() -> postWriter.create(post));
    }

    public void deletePostById(final Long postId, final Long userId) {
        Post post = postReader.findById(postId);
        User requestUser = userReader.findById(userId);

        checkSameUser(post.getUser(), requestUser);

        postWriter.delete(post);
    }

    public void updatePostById(final Long id,
                               final PostUpdateRequest request,
                               final Long userId) {
        Post post = postReader.findById(id);
        User requestUser = userReader.findById(userId);

        checkSameUser(post.getUser(), requestUser);

        postIntegrityRunnable(() -> postWriter.updateTitle(post, request));
    }

    /**
     * 함수형 인터페이스를 사용하여 post 쓰기 작업 시 발생하는 무결성 오류를 처리함.
     * uk_title 인덱스로 title 관련 중복이 발생할 경우 이를 catch하여 사용자에게 POST_TITLE_CONFLICT을 전달해줌
     *
     * @param runnable
     */
    private void postIntegrityRunnable(Runnable runnable) {
        try {
            runnable.run();
        } catch (DataIntegrityViolationException e) {
            if (e.getMessage().contains("title")) {
                throw new ConflictException(ErrorCode.POST_TITLE_CONFLICT);
            }
            throw e;
        }
    }

    private void checkSameUser(User user1, User user2) {
        if (!user1.equals(user2)) {
            throw new ForbiddenException(ErrorCode.NOT_ALLOWED_POST);
        }
    }
}
