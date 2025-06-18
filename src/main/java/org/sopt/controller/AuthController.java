package org.sopt.controller;

import org.sopt.annotation.ValidUserCreateRequest;
import org.sopt.dto.ResponseDto;
import org.sopt.dto.request.auth.LoginRequest;
import org.sopt.dto.request.user.UserCreateRequest;
import org.sopt.dto.response.auth.LoginResponse;
import org.sopt.dto.response.user.UserCreateResponse;
import org.sopt.service.auth.AuthService;
import org.sopt.service.user.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthController {
	private final AuthService authService;
	private final UserService userService;

	public AuthController(AuthService authService, UserService userService) {
		this.authService = authService;
		this.userService = userService;
	}

	/**
	 * 현재는 PW를 사용하지 않는 간단한 구조.
	 */
	@PostMapping("/login")
	public ResponseEntity<ResponseDto<LoginResponse>> login(@RequestBody LoginRequest request) {
		LoginResponse response = authService.login(request);
		return new ResponseEntity<>(ResponseDto.of(HttpStatus.OK, "로그인 성공", response), HttpStatus.OK);
	}

	@PostMapping("/sign-up")
	public ResponseEntity<ResponseDto<UserCreateResponse>> createUser(
		@RequestBody @ValidUserCreateRequest UserCreateRequest request) {
		UserCreateResponse response = userService.save(request);
		return new ResponseEntity<>(ResponseDto.of(HttpStatus.CREATED, "회원가입 성공", response), HttpStatus.CREATED);
	}

}
