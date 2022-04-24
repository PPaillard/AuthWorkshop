package com.wcs.authworkshop.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin
@RequestMapping("/tests")
public class TestController {
	
	@GetMapping("/all")
	public String recup() {
		return "je fonctionne !";
	}

	
	@PreAuthorize("hasRole('ADMIN')")
	@GetMapping("/admin")
	public String setAdmin() {
		return "Im the Admiiiinnn!";
	}
}
