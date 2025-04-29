package org.sopt.service.user;

import org.sopt.domain.User;
import org.sopt.dto.request.user.UserCreateRequest;
import org.sopt.repository.UserRepository;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public void saveUser(UserCreateRequest request) {
        User user = new User(request.name(), request.email());
        userRepository.save(user);
    }
}
