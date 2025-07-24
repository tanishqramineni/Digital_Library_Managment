package com.library.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.library.entity.Authenticate;
import com.library.entity.User;
import com.library.service.UserService;

@RestController
@RequestMapping("/user")
public class UserController {
	@Autowired
	private UserService userService;

	@PostMapping("/add")
	public ResponseEntity<User> addUser(@RequestBody User user) {
		try {
			User savedUser = userService.saveUser(user);
			return ResponseEntity.ok(savedUser);
		} catch (Exception e) {
			return ResponseEntity.badRequest().build();
		}
	}
	@PostMapping("/login")
	public String login(@RequestBody Authenticate auth)
	{
		return userService.verifyUser(auth.getUsername(),auth.getPassword());
	}
	@GetMapping("/getall")
	public List<User> getAllUsers() {
		return userService.getAllUsers();
	}

	@GetMapping("/getone/{username}")
	public ResponseEntity<User> getUserByname(@PathVariable String username) {
		Optional<User> user = userService.getUserByuserName(username);
		return user.map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
	}
	@PutMapping("/updatePassword")
	public String updatePass(@RequestBody Authenticate user){
		return userService.updatePassword(user.getUsername(), user.getPassword());
	}
	@PutMapping("/update/{username}")
	public ResponseEntity<User> updateUser(@RequestBody User user, @PathVariable String username) {
		User updatedUser = userService.updateUser(user, username);
		if (updatedUser != null) {
			return ResponseEntity.ok(updatedUser);
		}
		return ResponseEntity.notFound().build();
	}

	@DeleteMapping("/delete/{username}")
	public ResponseEntity<String> deleteUser(@PathVariable String username) {
		try {
			userService.deleteuser(username);
			return ResponseEntity.ok("User deleted successfully");
		} catch (Exception e) {
			return ResponseEntity.badRequest().body("Error deleting user");
		}
	}

	@GetMapping("/search/email/{email}")
	public ResponseEntity<User> getUserByEmail(@PathVariable String email) {
		Optional<User> user = userService.findByEmail(email);
		return user.map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
	}

	@GetMapping("/search/name/{name}")
	public List<User> searchUsersByName(@PathVariable String name) {
		return userService.findByUserName(name);
	}

	@GetMapping("/active")
	public List<User> getActiveUsers() {
		return userService.getActiveUsers();
	}

	@GetMapping("/status/{status}")
	public List<User> getUsersByStatus(@PathVariable String status) {
		return userService.getUsersByStatus(status);
	}

	@PutMapping("/suspend/{username}")
	public ResponseEntity<User> suspendUser(@PathVariable String username) {
		User suspendedUser = userService.suspendUser(username);
		if (suspendedUser != null) {
			return ResponseEntity.ok(suspendedUser);
		}
		return ResponseEntity.notFound().build();
	}

	@PutMapping("/activate/{username}")
	public ResponseEntity<User> activateUser(@PathVariable String username) {
		User activatedUser = userService.activateUser(username);
		if (activatedUser != null) {
			return ResponseEntity.ok(activatedUser);
		}
		return ResponseEntity.notFound().build();
	}

	@GetMapping("/count/active")
	public ResponseEntity<Long> getActiveUsersCount() {
		long count = userService.getActiveUsersCount();
		return ResponseEntity.ok(count);
	}

}
