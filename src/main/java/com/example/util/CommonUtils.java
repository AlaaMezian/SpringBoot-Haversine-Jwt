package com.example.util;

import java.util.ArrayList;

import javax.servlet.http.HttpServletRequest;

import org.springframework.util.StringUtils;

import com.example.constant.RoleName;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;

public class CommonUtils {

	public static RoleName getRoleFromJWT(HttpServletRequest request) {
		String token = getJwtFromRequest(request);

		Jws<Claims> claims = Jwts.parser().setSigningKey("JWTSuperSecretKey").parseClaimsJws(token);

		String userId = claims.getBody().get("id", String.class);
		ArrayList roleName = claims.getBody().get("Role", ArrayList.class);

		String rep = roleName.get(0).toString();
		String roleNameS = rep.replace("{authority=", "");
		String ro = roleNameS.substring(0, roleNameS.indexOf('}'));

		return RoleName.valueOf(ro);
	}

	public static long getIdFromJWT(HttpServletRequest request) {
		String token = getJwtFromRequest(request);

		Jws<Claims> claims = Jwts.parser().setSigningKey("JWTSuperSecretKey").parseClaimsJws(token);

		String userId = claims.getBody().get("id", String.class);

		Long userIdLong = Long.parseLong(userId);

		return userIdLong;
	}

	public static String getJwtFromRequest(HttpServletRequest request) {
		String bearerToken = request.getHeader("Authorization");
		if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
			return bearerToken.substring(7, bearerToken.length());
		}
		return null;
	}
}
