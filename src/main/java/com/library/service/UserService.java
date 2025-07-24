package com.library.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.library.entity.Books;
import com.library.entity.User;
import com.library.repository.UserRepository;
import com.library.service.JwtService;

@Service
public class UserService {
	@Autowired
	private UserRepository userRepository;
	@Autowired
	private PasswordEncoder passwordEncoder;
	
	@Autowired
	private AuthenticationManager authManager;
	
	@Autowired
	private JwtService jwtService;
	
	public User saveUser(User u) {
		u.setPassword(passwordEncoder.encode(u.getPassword()));
		return userRepository.save(u);
	}
	// Get All User
	public List<User> getAllUsers() {
		return userRepository.findAll();
	}

	public Optional<User> getUserByuserName(String username) {
		return userRepository.findUserName(username);
	}
	public String verifyUser(String username,String password) {
		Authentication authentication=authManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
		if(authentication.isAuthenticated())
		{
			return jwtService.generateToken(username);
		}
		else
		{
			return "Unsuccessfull";
		}
	}
	
	public String updatePassword(String username,String Password) {
		Optional<User> newUser =  userRepository.findUserName(username);
		if(newUser==null) return "There is no user registered with the UserName";
		User u1=newUser.get();
		u1.setPassword(passwordEncoder.encode(Password));
		userRepository.save(u1);
		return u1.getPassword()+"Password Changed Sucessfully"; 
	}
	public User updateUser(User u, String username) {
		Optional<User> theUser = userRepository.findUserName(username);
		if(theUser.isEmpty()) return null;
		User newUser = theUser.get();
		newUser.setUserName(u.getUserName());
		newUser.setEmail(u.getEmail());
		newUser.setPhone(u.getPhone());
		newUser.setRegistrationDate(u.getRegistrationDate());
		newUser.setAddress(u.getAddress());
		newUser.setPassword(u.getPassword());
		return userRepository.save(newUser);
	}

	public void deleteuser(String username) {
		Optional<User> u=userRepository.findUserName(username);
		if(u.isEmpty()) throw new RuntimeException("User is not Present");
		userRepository.deleteById(u.get().getUserId());
	}

	public Optional<User> findByEmail(String email) {
		return userRepository.findByEmail(email);
	}

	public List<User> findByUserName(String username) {
		return userRepository.findByUserNameContaining(username);
	}
	// Get active users
    public List<User> getActiveUsers() {
        return userRepository.findActiveUsers();
    }
    
    // Get users by status
    public List<User> getUsersByStatus(String status) {
        return userRepository.findByStatus(status);
    }

	// Suspend a user

	public User suspendUser(String username) {
		Optional<User> user = userRepository.findUserName(username);
		if (user.isPresent()) {
			User suspendUser = user.get();
			suspendUser.setStatus("SUSPENDED");
			return userRepository.save(suspendUser);
		}
		return null;
	}
	// Activate a user

	public User activateUser(String username) {
		Optional<User> user = userRepository.findUserName(username);
		if (user.isPresent()) {
			User suspendUser = user.get();
			suspendUser.setStatus("ACTIVE");
			return userRepository.save(suspendUser);
		}
		return null;
	}

	// Get total active users count
	public long getActiveUsersCount() {
		return userRepository.countActiveUsers();
	}

}
