package com.example.springBoot.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


import com.example.springBoot.repository.UserRepository;
import com.example.springBoot.entity.User;
import com.example.springBoot.exception.ResourceNotFoundException;

import java.util.List;

@RestController  //annotation for creating rest API
@RequestMapping("/api/users") //The path for all the rest APIs
public class UserController {
	
	@Autowired //Injected user repository annotation
	private UserRepository userRepository;
	
	//Get all users
	@GetMapping
	public List<User> getAllUsers(){
		return this.userRepository.findAll();
	}
	
	//Get the user by ID
	@GetMapping("/{id}")
	public User getUserByID(@PathVariable(value = "id") long userId) { 
		
		if(this.userRepository.existsById(userId)) {
			//The path variable gets it from database
			return this.userRepository.findById(userId).
				orElseThrow(() -> new ResourceNotFoundException("User not found with the ID: " + userId));
		}
		else {
			throw new ResourceNotFoundException("User not found with the ID: " + userId);
		}
	}
	
	//Create user
	@PostMapping
	public User createUser(@RequestBody User user) {
		String tk = user.generateToken(user.getID());
		user.setToken(tk);
		return this.userRepository.save(user);
		
	}
	
	//Update user
	@PutMapping("/{id}")
	public User updateUser(@RequestBody User user, @PathVariable("id") long userId) {
		
		if(this.userRepository.existsById(userId)) {
			User existingUserObj =  this.userRepository.findById(userId).
					orElseThrow(() -> new ResourceNotFoundException("User not found with the ID: " + userId));
			
			existingUserObj.setFirstName(user.getFirstName());
			existingUserObj.setLastName(user.getLastName());
			existingUserObj.setEmail(user.getEmail());
			existingUserObj.setPwd(user.getPwd());
			
			return this.userRepository.save(existingUserObj);	
			
		}
		else {
			throw new ResourceNotFoundException("User not found with the ID: " + userId);
		}	
	}
	
	//Delete user by ID 
	@DeleteMapping("/{id}/")
	public ResponseEntity<User> deleteUser(@PathVariable("id") long userId){
		
		if(this.userRepository.existsById(userId)) {
			User existingUserObj = this.userRepository.findById(userId).
					orElseThrow(() -> new ResourceNotFoundException("User not found with the ID: " + userId));
			this.userRepository.delete(existingUserObj);
			return ResponseEntity.ok().build();
		}
		else {
			throw new ResourceNotFoundException("User not found with the ID: " + userId);
		}
	}
	
	//Get userId by Token
	@GetMapping("/{Token}")
	public String getIdByToken(@RequestBody User user, @PathVariable("Token") String Token){
		String userId = "";
		try {
			User user2 = userRepository.searchByToken(Token);
			userId = String.valueOf(user2.getID());
			
		}
		catch(Exception e) {
			return "User does not exist";
		}
		return "User ID: " + userId;
	}
}