package com.example.security;

import java.util.Date;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import com.example.exception.BadRequestException;
import com.example.util.AppLogger;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.SignatureException;
import io.jsonwebtoken.UnsupportedJwtException;

@Component
public class JwtTokenProvider {

	@Value("${app.jwtSecret}")
	private String jwtSecret;

	@Value("${app.jwtExpirationInMs}")
	private int jwtExpirationInMs;

	public String generateToken(Authentication authentication) {

		AuthenticatedUser userPrincipal = (AuthenticatedUser) authentication.getPrincipal();

		Date now = new Date();
		Date expiryDate = new Date(now.getTime() + jwtExpirationInMs);

		return Jwts.builder().setSubject("Jwt Token").setIssuedAt(new Date()).setExpiration(expiryDate)
				.claim("id", Long.toString(userPrincipal.getId())).claim("Role", userPrincipal.getAuthorities())
				.signWith(SignatureAlgorithm.HS512, jwtSecret).compact();
	}

	public Long getUserIdFromJWT(String token) {
		Claims claims = Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(token).getBody();

		String idString = claims.get("id", String.class);
		Long idLong = Long.valueOf(idString);
		return idLong;
	}

	public boolean validateToken(String authToken) {
		try {
			Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(authToken);
			return true;
		} catch (SignatureException ex) {
			AppLogger.getInstance().error("Invalid JWT signature");
			throw new BadRequestException("Invalid JWT signature");
		} catch (MalformedJwtException ex) {
			AppLogger.getInstance().error("Invalid JWT token");
			throw new BadRequestException("Invalid JWT token");
		} catch (ExpiredJwtException ex) {
			AppLogger.getInstance().error("Expired JWT token");
			throw new BadRequestException("Expired Jwt Token");
		} catch (UnsupportedJwtException ex) {
			AppLogger.getInstance().error("Unsupported JWT token");
			throw new BadRequestException("Unsupported JWT token");
		} catch (IllegalArgumentException ex) {
			AppLogger.getInstance().error("JWT claims string is empty.");
		}
		return false;
	}
}
