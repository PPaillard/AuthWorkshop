package com.wcs.authworkshop.dto;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public class ConnectionDto {
	@NotNull
	@Size(min = 3, max = 50)
	private String username;
	
	/*
	 * Ajouter la verification du format (1maj, 1 chiffre, etc)
	 */
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
