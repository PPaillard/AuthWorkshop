package com.wcs.authworkshop.security.jwt;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.OncePerRequestFilter;

import com.wcs.authworkshop.security.service.UserDetailsImpl;
import com.wcs.authworkshop.security.service.UserDetailsServiceImpl;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureException;
import io.jsonwebtoken.UnsupportedJwtException;

public class AuthTokenFilter extends OncePerRequestFilter {

	@Value("${com.wcs.authworkshop.jwt.expirationDelay}")
	private int expirationDelay;
	
	@Value("${com.wcs.authworkshop.jwt.secretKey}")
	private String secretKey;
	
	@Autowired
	UserDetailsServiceImpl userDetailsServiceImpl;
	
	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		
		// On recupère l'entete de la requête
		String headerAuth = request.getHeader("Authorization");
		
		// on verifie si un token est présent
		if(headerAuth != null && headerAuth.startsWith("Bearer ")) {
			String token = headerAuth.substring(7);
			
			// On regarde s'il est valide sur tous ces aspects 
			// On le décode pour récupérer l'username 
			try {
				String username = Jwts.parser().setSigningKey(this.secretKey).parseClaimsJws(token).getBody().getSubject();
				
				// on va recupérer le userdetailsimpl relié à ce username
				UserDetailsImpl user = userDetailsServiceImpl.loadUserByUsername(username);
				
				// on stock ce userdetailsimpl dans le security context 
				// dans le but de l'avoir de coté pour plus tard (pour spring et nous même )
				UsernamePasswordAuthenticationToken upat = new UsernamePasswordAuthenticationToken(
						user, null, user.getAuthorities());
				
				upat.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
				
				SecurityContextHolder.getContext().setAuthentication(upat);
				
			} catch (SignatureException e) {
				System.out.println("Exception signature");
			} catch (MalformedJwtException e) {
				System.out.println("Jeton JWT malformé");
			} catch (ExpiredJwtException e) {
				System.out.println("Jeton JWT expiré");
			} catch (UnsupportedJwtException e) {
				System.out.println("Jeton non supporté");
			} catch (IllegalArgumentException e) {
				System.out.println("Illégal argument");
			}
		}
		// On informe Spring qu'il peut continuer ce qu'il faisait
		filterChain.doFilter(request, response);
	}

}
