package org.sopt.service.user;

import org.sopt.domain.User;
import org.sopt.dto.request.user.UserCreateRequest;
import org.sopt.dto.response.user.UserCreateResponse;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    private final UserReader userReader;
    private final UserWriter userWriter;

    public UserService(UserReader userReader, UserWriter userWriter) {
        this.userReader = userReader;
        this.userWriter = userWriter;
    }

    public UserCreateResponse save(UserCreateRequest request) {
        User user = new User(request.name(), request.email());
        userWriter.save(user);
        return new UserCreateResponse(user.getId(), user.getName());
    }
}
