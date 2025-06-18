package org.sopt.service.tag;

import java.util.List;

import org.sopt.domain.Tag;
import org.sopt.repository.TagRepository;
import org.springframework.stereotype.Service;

@Service
public class TagReader {
	private final TagRepository tagRepository;

	public TagReader(TagRepository tagRepository) {
		this.tagRepository = tagRepository;
	}

	public List<Tag> findAllByIds(List<Long> tagIds) {
		return tagRepository.findAllById(tagIds);
	}
}
