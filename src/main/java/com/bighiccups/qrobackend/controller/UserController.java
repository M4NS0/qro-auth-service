package com.bighiccups.qrobackend.controller;

import com.bighiccups.qrobackend.model.JwtRequest;
import com.bighiccups.qrobackend.security.JwtTokenUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin(origins="*")
@RequestMapping("/user")
public class UserController {

	private final JwtTokenUtil jwtTokenUtil;

	@Autowired
	public UserController(JwtTokenUtil jwtTokenUtil) {
		this.jwtTokenUtil = jwtTokenUtil;
	}

	@GetMapping("/allusers")
	public String displayUsers() {
		return "Display All Users";
	}
	
	@GetMapping("/displayuser")
	@PreAuthorize("hasRole('ROLE_USER') or hasRole('ROLE_ADMIN')")
	public String displayToUser() {
		return "Display to both user and admin";
	}
	
	@GetMapping("/displayadmin")
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	public String displayToAdmin() {
		return "Display only to admin";
	}

	@GetMapping("/getuserbyjwt")
	@PreAuthorize("hasRole('ROLE_USER') or hasRole('ROLE_ADMIN')")
	public String getUserByJwt(@RequestBody JwtRequest jwtRequest) {
		String token = jwtRequest.getToken();
		return jwtTokenUtil.getUserNameFromJwtToken(token);
	}
	// make it return a user object to future microservice
	// adjust all messages mocking it into pmessage.properties.en //pt

}
