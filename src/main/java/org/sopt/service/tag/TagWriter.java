package org.sopt.service.tag;

import java.util.List;

import org.sopt.domain.Post;
import org.sopt.domain.PostTag;
import org.sopt.domain.Tag;
import org.sopt.repository.PostTagRepository;
import org.sopt.repository.TagRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class TagWriter {
	private final TagRepository tagRepository;
	private final PostTagRepository postTagRepository;

	public TagWriter(TagRepository tagRepository, PostTagRepository postTagRepository) {
		this.tagRepository = tagRepository;
		this.postTagRepository = postTagRepository;
	}

	@Transactional
	public void createPostTag(Post post, List<Tag> tags) {
		List<PostTag> postTags = postTagRepository.saveAll(tags.stream()
			.map(tag -> PostTag.create(post, tag))
			.toList());
		post.addPostTags(postTags);
	}
}
