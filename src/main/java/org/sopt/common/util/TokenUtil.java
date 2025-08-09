package org.sopt.common.util;

public class TokenUtil {
	private final static String BEARER_PREFIX = "Bearer ";
	private final static String ENCODED_SPACE = "%20";

	public static String removeBearerPrefix(String bearerToken) {
		return bearerToken.substring(BEARER_PREFIX.length());
	}

	public static String addBearerPrefix(String token) {
		String bearerToken = BEARER_PREFIX + token;
		return bearerToken.replaceAll("\\+", ENCODED_SPACE);
	}

}
