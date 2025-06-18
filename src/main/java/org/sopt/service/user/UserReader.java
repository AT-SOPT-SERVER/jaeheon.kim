package org.sopt.service.user;

import static org.sopt.constant.CacheConstant.*;

import org.sopt.domain.User;
import org.sopt.exception.NotFoundException;
import org.sopt.exception.errorcode.ErrorCode;
import org.sopt.repository.UserRepository;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

@Service
public class UserReader {
	private final UserRepository userRepository;

	public UserReader(UserRepository userRepository) {
		this.userRepository = userRepository;
	}

	@Cacheable(cacheNames = USER_CACHE_NAME, key = USER_CACHE_KEY)
	public User findById(Long userId) {
		return userRepository.findById(userId).orElseThrow(()
			-> new NotFoundException(ErrorCode.USER_NOT_FOUND));
	}

}
