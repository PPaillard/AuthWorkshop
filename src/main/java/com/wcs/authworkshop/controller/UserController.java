package com.wcs.authworkshop.controller;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.wcs.authworkshop.dto.ConnectionDto;
import com.wcs.authworkshop.dto.ConnectionResponseDto;
import com.wcs.authworkshop.dto.UserDto;
import com.wcs.authworkshop.entity.ERole;
import com.wcs.authworkshop.entity.Role;
import com.wcs.authworkshop.entity.User;
import com.wcs.authworkshop.repository.RoleRepository;
import com.wcs.authworkshop.repository.UserRepository;
import com.wcs.authworkshop.security.jwt.JWTUtils;
import com.wcs.authworkshop.security.service.UserDetailsImpl;

@RestController
@RequestMapping("/auth")
public class UserController {

	@Autowired
	UserRepository userRepository;

	@Autowired
	RoleRepository roleRepository;

	@Autowired
	PasswordEncoder passwordEncoder;

	@Autowired
	AuthenticationManager authenticationManager;

	@Autowired
	JWTUtils jwtUtils;

	@PostMapping("/signin")
	public ConnectionResponseDto connect(@Valid @RequestBody(required = true) ConnectionDto connectionDto) {
		// Connecter l'utilisateur
		Authentication authentication = authenticationManager.authenticate(
				new UsernamePasswordAuthenticationToken(connectionDto.getUsername(), connectionDto.getPassword()));
		// On place l'objet d'authentification (qui contient le userdetailsimpl)
		// dans le securitycontext pour y avoir accès partout.
		SecurityContextHolder.getContext().setAuthentication(authentication);

		// on récupère le user dans le context
		UserDetailsImpl userDetailsImpl = (UserDetailsImpl) authentication.getPrincipal();

		String token = jwtUtils.generateToken(authentication);

		return new ConnectionResponseDto(userDetailsImpl.getUsername(), userDetailsImpl.getAuthorities(), token);
	}

	@PostMapping("/signup")
	public void create(@Valid @RequestBody(required = true) UserDto userDto) {
		// On verifie que le username et le mail n'existe pas déjà
		if (userRepository.existsByEmail(userDto.getEmail())
				|| userRepository.existsByUsername(userDto.getUsername())) {
			// si c'est le cas, on balance une exception
			throw new ResponseStatusException(HttpStatus.CONFLICT);
		}

		// on va chercher le rôle USER
		Role role = roleRepository.findByAuthority(ERole.ROLE_USER.name())
				.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

		// on créé le user avec les informations données
		User user = new User();
		user.setUsername(userDto.getUsername());
		user.setEmail(userDto.getEmail());
		user.setPassword(passwordEncoder.encode(userDto.getPassword()));
		user.setCreation(LocalDate.now());
		// on lui ajoute son rôle de base
		Set<Role> roles = new HashSet<>();
		roles.add(role);
		user.setAuthorities(roles);
		userRepository.save(user);
	}
	
	@DeleteMapping("/{id}")
	public void delete(@PathVariable(required = true) int id) {
		userRepository.deleteById(id);
	}
	
}
