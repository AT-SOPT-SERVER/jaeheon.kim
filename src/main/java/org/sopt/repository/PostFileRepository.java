package org.sopt.repository;

import org.sopt.domain.Post;
import org.sopt.util.IdGenerator;
import org.sopt.util.PostUtil;

import java.io.*;
import java.util.List;
import java.util.function.Function;

import static org.sopt.constant.PostConstant.DELIMITER;

public class PostFileRepository implements PostRepository{

    public static final String POST_DATA_FILE_PATH = "src/main/resources/posts.md";

    private final IdGenerator<Long> idGenerator;

    public PostFileRepository(IdGenerator<Long> idGenerator){
        this.idGenerator = idGenerator;
    }

    public static Long lastAutoIncrementValue(){
        return readFunction(bufferedReader -> bufferedReader.lines()
                .map(PostUtil::parsePost)
                .max((post1, post2) -> Long.compare(post1.getId(), post2.getId()))
                .map(Post::getId)
                .orElse(0L));
    }

    @Override
    public synchronized void save(Post post) {
        Long newId = idGenerator.generateId();
        post.setId(newId);

        writeConsume(bufferedWriter -> {
            bufferedWriter
                    .append(String.valueOf(newId))
                    .append(DELIMITER)
                    .append(post.getTitle())
                    .append("\n");
        }, true);
    }

    @Override
    public List<Post> findAll() {
        return readFunction(bufferedReader -> bufferedReader.lines()
                    .map(PostUtil::parsePost)
                    .toList());
    }

    @Override
    public Post findPostById(Long id) {
        return readFunction(bufferedReader -> bufferedReader.lines()
                .map(PostUtil::parsePost)
                .filter(post -> post.getId().equals(id))
                .findFirst()
                .orElse(null));
    }

    /**
     * 특정 줄만 제거하는 방법을 찾지 못해서 전체 파일을 재작성하고 있음,,,
     * @param id
     * @return
     */
    @Override
    public synchronized boolean deletePostById(Long id) {
        List<Post> allPosts = findAll();

        List<Post> updatedPosts = allPosts.stream()
                .filter(post -> !post.getId().equals(id))
                .toList();

        if(allPosts.size() == updatedPosts.size()){
            return false;
        }

        writeConsume(bufferedWriter ->
                bufferedWriter.write(updatedPostsFileContent(updatedPosts)) , false);
        return true;
    }

    @Override
    public synchronized boolean updatePostTitle(Long id, String newTitle) {
        List<Post> allPosts = findAll();

        if(findPostById(id) == null){
            return false;
        }

        allPosts.stream()
                .map(post -> post.getId().equals(id)
                        ? post.updateTitle(newTitle) : post)
                .toList();

        writeConsume(bufferedWriter ->
                bufferedWriter.write(updatedPostsFileContent(allPosts)), false);
        return true;
    }

    @Override
    public List<Post> findAllByKeyword(String keyword) {
        return findAll().stream()
                .filter(post -> post.getTitle().contains(keyword))
                .toList();
    }

    @Override
    public boolean isDuplicatedTitle(String title) {
        return findAll().stream()
                .map(Post::getTitle)
                .anyMatch(title::equals);
    }

    /**
     * IOException 을 던질 수 있도록 커스텀 functionInterface 생성
     * @param <T>
     */
    @FunctionalInterface
    public interface IOConsumer<T>{
        void accept(T t) throws IOException ;
    }

    private String updatedPostsFileContent(List<Post> posts){
        StringBuilder stringBuilder = new StringBuilder();
        posts.forEach(post -> stringBuilder.append(post.getId())
                .append(DELIMITER)
                .append(post.getTitle())
                .append("\n"));
        return stringBuilder.toString();
    }

    private static <T> T readFunction(Function<BufferedReader, T> function){
        try (BufferedReader bufferedReader = new BufferedReader(new FileReader(POST_DATA_FILE_PATH))) {
            return function.apply(bufferedReader);
        } catch (FileNotFoundException e) {
            throw new RuntimeException("FileNotFound: " + e.getMessage());
        } catch (IOException e) {
            throw new RuntimeException("FileReadError: " + e.getMessage());
        }
    }

    private static void writeConsume(IOConsumer<BufferedWriter> consumer, Boolean append){
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(POST_DATA_FILE_PATH, append))) {
            consumer.accept(writer);
        } catch (IOException e) {
            throw new RuntimeException("FileWriteError: " + e.getMessage());
        }
    }

}
