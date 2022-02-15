package com.wcs.authworkshop.dto;

import java.util.List;

import com.wcs.authworkshop.entity.Role;

public class ConnectionResponseDto {

	private String username;
	
	private List<Role> authorities;
	
	private String token;
	
	public ConnectionResponseDto(String username, List<Role> authorities, String token) {
		this.username = username;
		this.authorities = authorities;
		this.token = token;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public void setAuthorities(List<Role> authorities) {
		this.authorities = authorities;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public String getUsername() {
		return username;
	}

	public List<Role> getAuthorities() {
		return authorities;
	}

	public String getToken() {
		return token;
	}
}
