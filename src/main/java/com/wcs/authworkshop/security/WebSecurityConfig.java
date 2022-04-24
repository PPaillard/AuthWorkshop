package com.wcs.authworkshop.security;

import java.util.Arrays;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import com.wcs.authworkshop.security.jwt.AuthEntryPointJwt;
import com.wcs.authworkshop.security.jwt.AuthTokenFilter;
import com.wcs.authworkshop.security.service.UserDetailsServiceImpl;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

	@Value("${com.familleJaffre.p3.urlCors}")
	private String urlsCors;
	
	@Autowired
	UserDetailsServiceImpl userDetailsServiceImpl;

	@Autowired
	private AuthEntryPointJwt unauthorizedHandler;

	@Bean
	public AuthTokenFilter getFilterToken() {
		return new AuthTokenFilter();
	}

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		// activation des cors & desactivation des CSRF
		http.cors().and().csrf().disable().exceptionHandling().authenticationEntryPoint(unauthorizedHandler);
		http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
		// these path are accessible by everybody (example)
		http.authorizeRequests().antMatchers("/auth/**", "/password/**", "/photos/**").permitAll()
		// all the other paths are accessible only if authentication is done
		.anyRequest().authenticated();

		http.addFilterBefore(getFilterToken(), UsernamePasswordAuthenticationFilter.class);
	}
	
	/*
	 * Config globale des CORS de l'application
	 */
	@Bean
	CorsConfigurationSource corsConfigurationSource() {
		CorsConfiguration configuration = new CorsConfiguration();
		// we set authorized urls to the CORS
		// we retrieve what we stocked in application.properties converted in List<String>
		configuration.setAllowedOrigins(Arrays.asList(urlsCors.split(",")));
		configuration.setAllowedMethods(Arrays.asList("*"));
        configuration.setAllowedHeaders(Arrays.asList("*"));
        configuration.setAllowCredentials(true);
		UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
		// Cors will applied on this path
		source.registerCorsConfiguration("/**", configuration);
		return source;
	}

	@Bean
	@Override
	public AuthenticationManager authenticationManagerBean() throws Exception {
		// Permet d'utiliser l'authentication manager ailleurs que dans le
		// websecurityConfig
		return super.authenticationManagerBean();
	}

	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		// configure Spring security pour savoir quel service il doit utiliser pour
		// recup le user
		// et encoder le mot de passe pour verifier la correspondance
		auth.userDetailsService(userDetailsServiceImpl).passwordEncoder(getPasswordEncoder());
	}

	@Bean
	public PasswordEncoder getPasswordEncoder() {
		return new BCryptPasswordEncoder();
	}
}
