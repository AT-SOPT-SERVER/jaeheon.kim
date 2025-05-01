package org.sopt.service.user;

import org.sopt.domain.User;
import org.sopt.repository.UserRepository;
import org.springframework.stereotype.Service;

@Service
public class UserWriter {
    private final UserRepository userRepository;

    public UserWriter(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public void save(User user) {
        userRepository.save(user);
    }
}
