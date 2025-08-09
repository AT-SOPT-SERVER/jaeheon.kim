package org.sopt.service.auth;

import java.util.Date;

import org.sopt.authentication.JwtProvider;
import org.sopt.dto.request.auth.LoginRequest;
import org.sopt.dto.response.auth.LoginResponse;
import org.springframework.stereotype.Service;

@Service
public class AuthService {
	private final JwtProvider jwtProvider;

	public AuthService(final JwtProvider jwtProvider) {
		this.jwtProvider = jwtProvider;
	}

	public LoginResponse login(final LoginRequest request) {
		Date currentDate = new Date();
		return new LoginResponse(jwtProvider.createToken(request.userId().toString(), currentDate));
	}
}
