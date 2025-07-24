package com.library.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.library.entity.LibraryUserPrincipal;
import com.library.entity.User;
import com.library.repository.UserRepository;


public class LibraryUserDetailsService implements UserDetailsService{
	@Autowired
	private UserRepository userRepo;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		User user = userRepo.findUserName(username)
	            .orElseThrow(() -> new UsernameNotFoundException("User not found with username: " + username));
		return new LibraryUserPrincipal(user);
	}
}
