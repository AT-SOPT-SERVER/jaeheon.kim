package org.sopt.authentication;

import java.nio.charset.StandardCharsets;
import java.util.Date;

import javax.crypto.SecretKey;

import org.sopt.exception.UnAuthorizedException;
import org.sopt.exception.errorcode.ErrorCode;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.security.Keys;

@Component
public class JwtProvider {
	private final SecretKey key;
	private final long accessTokenValidityInMilliseconds;

	public JwtProvider(
		@Value("${security.jwt.token.secret-key}") final String secretKey,
		@Value("${security.jwt.token.expire-length}") final long accessTokenValidityInMilliseconds) {
		this.key = Keys.hmacShaKeyFor(secretKey.getBytes(StandardCharsets.UTF_8));
		this.accessTokenValidityInMilliseconds = accessTokenValidityInMilliseconds;
	}

	public String createToken(final String payload, final Date currentDate) {
		Date expiration = new Date(currentDate.getTime() + accessTokenValidityInMilliseconds);

		return Jwts.builder()
			.subject(payload)
			.issuedAt(currentDate)
			.expiration(expiration)
			.signWith(key)
			.compact();
	}

	public String getPayload(final String token) {
		try {
			return Jwts.parser()
				.verifyWith(key)
				.build()
				.parseSignedClaims(token)
				.getPayload()
				.getSubject();
		} catch (ExpiredJwtException e) {
			throw new UnAuthorizedException(ErrorCode.EXPIRED_TOKEN);
		} catch (UnsupportedJwtException e) {
			throw new UnAuthorizedException(ErrorCode.UNSUPPORTED_TOKEN);
		} catch (JwtException e) {
			throw new UnAuthorizedException(ErrorCode.INVALID_TOKEN);
		}
	}
}
