package com.wcs.authworkshop.security.service;

import java.util.List;

import org.springframework.security.core.userdetails.UserDetails;

import com.wcs.authworkshop.entity.Role;
import com.wcs.authworkshop.entity.User;

public class UserDetailsImpl implements UserDetails{

	private Long id;
	
	private String email;
	
	private String username;
	
	private String password;
	
	private List<Role> authorities;
	
	public static UserDetailsImpl build(User user) {
		UserDetailsImpl userDetailsImpl = new UserDetailsImpl();
		userDetailsImpl.setId(user.getId());
		userDetailsImpl.setUsername(user.getUsername());
		userDetailsImpl.setEmail(user.getEmail());
		userDetailsImpl.setPassword(user.getPassword());
		userDetailsImpl.setAuthorities(user.getAuthorities());
		return userDetailsImpl;
	}
	
	@Override
	public List<Role> getAuthorities() {
		return this.authorities;
	}

	@Override
	public String getPassword() {
		return this.password;
	}

	@Override
	public String getUsername() {
		return this.username;
	}

	@Override
	public boolean isAccountNonExpired() {
		return true;
	}

	@Override
	public boolean isAccountNonLocked() {
		return true;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}

	@Override
	public boolean isEnabled() {
		return true;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public void setAuthorities(List<Role> authorities) {
		this.authorities = authorities;
	}

}
