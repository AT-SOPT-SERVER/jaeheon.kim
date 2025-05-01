package org.sopt.controller;

import org.sopt.dto.ResponseDto;
import org.sopt.dto.request.user.UserCreateRequest;
import org.sopt.dto.response.user.UserCreateResponse;
import org.sopt.service.user.UserService;
import org.springframework.http.HttpStatus;
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

    @PostMapping("/sign-up")
    public ResponseEntity<ResponseDto<UserCreateResponse>> createUser(@RequestBody UserCreateRequest request) {
        UserCreateResponse response = userService.save(request);
        return new ResponseEntity<>(ResponseDto.of(HttpStatus.CREATED, "회원가입 성공", response), HttpStatus.CREATED);
    }
    
}
