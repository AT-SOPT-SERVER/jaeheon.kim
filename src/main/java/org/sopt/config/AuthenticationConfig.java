package org.sopt.config;

import org.sopt.authentication.AuthenticationInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class AuthenticationConfig implements WebMvcConfigurer {

	private static final String[] EXCLUDE_PATH_PATTERNS = {"/auth/sign-up", "/auth/login"};
	private final AuthenticationInterceptor authenticationInterceptor;

	public AuthenticationConfig(AuthenticationInterceptor authenticationInterceptor) {
		this.authenticationInterceptor = authenticationInterceptor;
	}

	@Override
	public void addInterceptors(final InterceptorRegistry registry) {
		registry.addInterceptor(authenticationInterceptor)
			.excludePathPatterns(EXCLUDE_PATH_PATTERNS);
	}
}
