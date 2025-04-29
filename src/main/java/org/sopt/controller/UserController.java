package org.sopt.controller;

import org.sopt.dto.request.user.UserCreateRequest;
import org.sopt.service.user.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping()
    public ResponseEntity<?> createUser(@RequestBody UserCreateRequest request) {
        userService.saveUser(request);
        return ResponseEntity.ok().build();
    }


}
