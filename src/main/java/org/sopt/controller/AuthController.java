package org.sopt.controller;

import java.nio.charset.StandardCharsets;
import java.util.Base64;

import org.sopt.domain.User;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@RestController
@RequestMapping("/auth")
public class AuthController {

	@GetMapping("/login")
	public ResponseEntity<?> login(HttpServletRequest request) {
		String authHeader = request.getHeader("Authorization");
		if (authHeader == null || !authHeader.startsWith("Basic")) {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("헤더가 이상해");
		}

		String decodedString = authHeader.substring("Basic ".length());

		byte[] decodedByte = Base64.getDecoder().decode(decodedString);

		String credential = new String(decodedByte, StandardCharsets.UTF_8);

		String[] ports = credential.split(":");

		String userName = ports[0];
		String password = ports[1];

		if (userName.equals("sopt36") && password.equals("server")) {
			return ResponseEntity.ok("인증 성공");
		}
		return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("인증 실패");
	}

	@GetMapping("/set-cookie")
	public ResponseEntity<String> setCookie(
		HttpServletRequest request,
		HttpServletResponse response
	) {
		String username = "userSopt";
		String password = "sopt1234";

		Cookie userNameCookie = new Cookie("userId", username);
		Cookie passwordCookie = new Cookie("password", password);

		userNameCookie.setPath("/");
		passwordCookie.setPath("/");

		response.addCookie(userNameCookie);
		response.addCookie(passwordCookie);

		return ResponseEntity.ok("쿠키를 잘 구웟듭니댱, 내가 만든 쿠키~~~~ 너를 위해 궈웟지 ♥");
	}

	@GetMapping("/get-cookie")
	public ResponseEntity<String> getCookie(
		@CookieValue("userId") String userId,
		@CookieValue("password") String password
	) {
		return ResponseEntity.ok("받은 쿠키" + "유저 아이디 : " + userId + "유저 비밀번호 : " + password);
	}

	@PostMapping("/login")
	public ResponseEntity<String> loginPost(HttpServletRequest request) {
		String userId = "userSopt";
		String password = "sopt1234";

		if (userId.equals("userSopt") && password.equals("sopt1234")) {
			HttpSession session = request.getSession(true);
			session.setAttribute("user", new User(userId, "mail"));
			return ResponseEntity.ok().body("세션 저장 완료");
		}

		throw new RuntimeException("인증 실패 시");
	}
}
