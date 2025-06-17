package org.sopt.authentication;

import org.sopt.common.util.TokenUtil;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class AuthenticationInterceptor implements HandlerInterceptor {

	private final static String AUTHENTICATION_HEADED = "Authorization";
	private final static String PRINCIPAL_HEADER = "userId";

	private final JwtProvider jwtProvider;

	public AuthenticationInterceptor(final JwtProvider jwtProvider) {
		this.jwtProvider = jwtProvider;
	}

	@Override
	public boolean preHandle(final HttpServletRequest request, final HttpServletResponse response,
		final Object handler) throws
		Exception {
		String bearerToken = request.getHeader(AUTHENTICATION_HEADED);
		if (isRequiredFalse(request) && bearerToken == null) {
			return HandlerInterceptor.super.preHandle(request, response, handler);
		}

		String accessToken = TokenUtil.removeBearerPrefix(bearerToken);

		CustomHttpServletRequestWrapper requestWrapper = new CustomHttpServletRequestWrapper(request);

		String userId = jwtProvider.getPayload(accessToken);
		requestWrapper.addHeader(PRINCIPAL_HEADER, userId);

		return HandlerInterceptor.super.preHandle(requestWrapper, response, handler);
	}

	private boolean isRequiredFalse(HttpServletRequest request) {
		return request.getRequestURI().equals("/posts") && request.getMethod().equals(HttpMethod.GET.name());
	}
}
