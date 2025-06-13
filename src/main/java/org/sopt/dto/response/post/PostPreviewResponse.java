package org.sopt.dto.response.post;

import java.util.List;

import org.sopt.domain.Post;
import org.sopt.domain.PostTag;
import org.sopt.domain.Tag;

public class PostPreviewResponse {

	private Long postId;
	private String title;
	private Long writerId;
	private String writerName;
	private int likeCount;
	private int commentCount;
	private boolean isLiked;
	private List<String> tags;

	protected PostPreviewResponse() {
	}

	public PostPreviewResponse(Long postId, String title, Long writerId, String writerName, int likeCount,
		int commentCount,
		boolean isLiked, List<String> tags) {
		this.postId = postId;
		this.title = title;
		this.writerId = writerId;
		this.writerName = writerName;
		this.likeCount = likeCount;
		this.commentCount = commentCount;
		this.isLiked = isLiked;
		this.tags = tags;
	}

	public PostPreviewResponse(Post post, boolean isLiked) {
		this.postId = post.getId();
		this.title = post.getTitle();
		this.writerId = post.getUser().getId();
		this.writerName = post.getUser().getName();
		this.likeCount = post.getLikeCount();
		this.commentCount = post.getCommentCount();
		this.isLiked = isLiked;
		this.tags = post.getPostTags().stream()
			.map(PostTag::getTag)
			.map(Tag::getKoreanName)
			.toList();
	}

	public Long getPostId() {
		return postId;
	}

	public String getTitle() {
		return title;
	}

	public Long getWriterId() {
		return writerId;
	}

	public String getWriterName() {
		return writerName;
	}

	public int getLikeCount() {
		return likeCount;
	}

	public int getCommentCount() {
		return commentCount;
	}

	public boolean isLiked() {
		return isLiked;
	}

	public List<String> getTags() {
		return tags;
	}

}

