package org.sopt.service.post;

import static org.sopt.constant.CacheConstant.*;

import java.util.Optional;

import org.sopt.domain.Post;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.stereotype.Service;

@Service
public class PostCacheService {
	private final CacheManager cacheManager;

	public PostCacheService(CacheManager cacheManager) {
		this.cacheManager = cacheManager;
	}

	public void putPost(Post post) {
		Cache postCache = cacheManager.getCache(POST_CACHE_NAME);
		assert postCache != null : "postCache 를 찾을 수 없음.";

		postCache.put(post.getId(), post);
	}

	public Optional<Post> getCachedAllRelationInitializedPost(Long postId) {
		Cache postCache = cacheManager.getCache(POST_CACHE_NAME);

		if (postCache == null) {
			return Optional.empty();
		}
		Cache.ValueWrapper wrapper = postCache.get(postId);

		if (wrapper != null && wrapper.get() instanceof Post post) {
			if (!post.isRelatedEntityProxy()) {
				return Optional.of(post);
			}
		}
		return Optional.empty();
	}
}
