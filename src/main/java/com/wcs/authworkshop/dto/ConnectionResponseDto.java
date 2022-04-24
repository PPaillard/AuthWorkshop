package com.wcs.authworkshop.dto;

import java.util.Set;

import com.wcs.authworkshop.entity.Role;

public class ConnectionResponseDto {

	private String username;
	
	private Set<Role> authorities;
	
	private String token;
	
	public ConnectionResponseDto(String username, Set<Role> authorities, String token) {
		this.username = username;
		this.authorities = authorities;
		this.token = token;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public void setAuthorities(Set<Role> authorities) {
		this.authorities = authorities;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public String getUsername() {
		return username;
	}

	public Set<Role> getAuthorities() {
		return authorities;
	}

	public String getToken() {
		return token;
	}
}
