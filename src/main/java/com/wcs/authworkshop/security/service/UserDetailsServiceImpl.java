package com.wcs.authworkshop.security.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.wcs.authworkshop.entity.User;
import com.wcs.authworkshop.repository.UserRepository;

@Service
public class UserDetailsServiceImpl  implements UserDetailsService{
	
	@Autowired
	UserRepository userRepository;

	@Override
	public UserDetailsImpl loadUserByUsername(String username) throws UsernameNotFoundException {
		// méthode utilisée de manière générique pour que Spring aille récup l'utilisateur qu'il veut connecter
		User user = userRepository.findByUsername(username)
				.orElseThrow(()-> new UsernameNotFoundException("User with the name "+username+" not found !"));
		
		return UserDetailsImpl.build(user);
	}

}
