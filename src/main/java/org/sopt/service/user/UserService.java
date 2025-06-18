package org.sopt.service.user;

import static org.sopt.constant.CacheConstant.*;

import org.sopt.domain.User;
import org.sopt.dto.request.user.UserCreateRequest;
import org.sopt.dto.response.user.UserCreateResponse;
import org.springframework.cache.annotation.CachePut;
import org.springframework.stereotype.Service;

@Service
public class UserService {

	private final UserReader userReader;
	private final UserWriter userWriter;

	public UserService(UserReader userReader, UserWriter userWriter) {
		this.userReader = userReader;
		this.userWriter = userWriter;
	}

	@CachePut(value = USER_CACHE_NAME, key = "#user.id")
	public UserCreateResponse save(UserCreateRequest request) {
		User user = new User(request.name(), request.email());
		userWriter.save(user);
		return new UserCreateResponse(user.getId(), user.getName());
	}

}
