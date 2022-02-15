package com.wcs.authworkshop.security.jwt;

import java.util.Date;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import com.wcs.authworkshop.security.service.UserDetailsImpl;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

@Component
public class JWTUtils {
	
	@Value("${com.wcs.authworkshop.jwt.expirationDelay}")
	private int expirationDelay;
	
	@Value("${com.wcs.authworkshop.jwt.secretKey}")
	private String secretKey;

	public String generateToken(Authentication authentication) {
		
		UserDetailsImpl user = (UserDetailsImpl)authentication.getPrincipal();
		
		return Jwts.builder().setSubject(user.getUsername())
					.setIssuedAt(new Date())
					.setExpiration(new Date((new Date()).getTime()+this.expirationDelay))
					.signWith(SignatureAlgorithm.HS512, secretKey)
					.compact();
	}
}
