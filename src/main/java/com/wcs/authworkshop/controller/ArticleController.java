package com.wcs.authworkshop.controller;

import java.util.HashSet;
import java.util.Set;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.wcs.authworkshop.entity.Article;
import com.wcs.authworkshop.entity.User;
import com.wcs.authworkshop.repository.ArticleRepository;
import com.wcs.authworkshop.repository.UserRepository;
import com.wcs.authworkshop.security.service.UserDetailsImpl;

@RestController
@RequestMapping("/articles")
public class ArticleController {
	
	@Autowired
	ArticleRepository articleRepository;
	
	@Autowired
	UserRepository userRepository;

	@PostMapping
	public void add(@Valid @RequestBody Article article) {
		
		// on va chercher le user connecté
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		UserDetailsImpl userDetailsImpl = (UserDetailsImpl) authentication.getPrincipal();
		
		// on rappatrie le user avec son ID
		User user = userRepository.getById(userDetailsImpl.getId());
		// on créé l'article avec le user connecté
		Set<User> users = new HashSet<>() ;
		users.add(user);
		article.setUsers(users);
		articleRepository.save(article);
	}
	
	@DeleteMapping("/{id}")
	public void delete(@PathVariable(required = true) int id) {
		articleRepository.deleteById(id);
	}
	
	
}
