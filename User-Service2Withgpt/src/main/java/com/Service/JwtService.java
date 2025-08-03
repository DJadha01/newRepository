package com.Service;

import java.util.Map;
import java.util.function.Function;

import javax.servlet.http.HttpServletRequest;

import com.Security.CustomeUserService;

import io.jsonwebtoken.Claims;

public interface JwtService {
	
	String extractUsername(String token);

	<T> T extractClaim(String token, Function<Claims, T> claimsResolver);

	String generateToken(CustomeUserService userDetails);

	String generateToken(Map<String, Object> extraClaims, CustomeUserService userDetails);

	long getExpirationTime();

	boolean isTokenValid(String token, CustomeUserService userDetails);

	String extractTokenFromRequest(HttpServletRequest request);


}
