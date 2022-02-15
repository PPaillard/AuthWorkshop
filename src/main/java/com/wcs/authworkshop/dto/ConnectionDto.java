package com.wcs.authworkshop.dto;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public class ConnectionDto {
	@NotNull
	@Size(min = 3, max = 50)
	private String username;
	
	@NotNull
	@Size(min = 3, max = 120)
	private String password;

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

}
