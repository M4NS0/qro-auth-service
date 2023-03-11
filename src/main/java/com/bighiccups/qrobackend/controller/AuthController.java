package com.bighiccups.qrobackend.controller;

import java.util.*;
import java.util.stream.Collectors;
import javax.validation.Valid;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import com.bighiccups.qrobackend.dao.RoleRepository;
import com.bighiccups.qrobackend.dao.UserRepository;
import com.bighiccups.qrobackend.model.AuthResponse;
import com.bighiccups.qrobackend.model.CustomUserBean;
import com.bighiccups.qrobackend.model.Role;
import com.bighiccups.qrobackend.model.Roles;
import com.bighiccups.qrobackend.model.SignupRequest;
import com.bighiccups.qrobackend.model.User;
import com.bighiccups.qrobackend.security.JwtTokenUtil;

@Slf4j
@RestController
@CrossOrigin(origins="http://localhost:4200") 
@RequestMapping("/auth")
public class AuthController {

	@Autowired
	UserRepository userRepository;
	@Autowired
	RoleRepository roleRepository;
	@Autowired
	PasswordEncoder encoder;
	@Autowired
	AuthenticationManager authenticationManager;
	@Autowired
	JwtTokenUtil jwtTokenUtil;
	
	@PostMapping("/login")
	public ResponseEntity<?> userLogin(@Valid @RequestBody User user) {
		log.info("User login request received");
		Authentication authentication = authenticationManager.authenticate(
					new UsernamePasswordAuthenticationToken(user.getUserName(), user.getPassword()));
		
		SecurityContextHolder.getContext().setAuthentication(authentication);
		String token = jwtTokenUtil.generateJwtToken(authentication);
		CustomUserBean userBean = (CustomUserBean) authentication.getPrincipal();		
		List<String> roles = userBean.getAuthorities().stream()
									 .map(auth -> auth.getAuthority())
									 .collect(Collectors.toList());
		AuthResponse authResponse = new AuthResponse();
		authResponse.setToken(token);
		authResponse.setRoles(roles);
		return ResponseEntity.ok(authResponse);
	}
	
	@PostMapping("/signup")
	public ResponseEntity<?> userSignup(@Valid @RequestBody SignupRequest signupRequest,
										@RequestHeader("Accept-Language") Locale locale) {
		log.info("User signup request received");
		ResourceBundle messages = ResourceBundle.getBundle("i18n/messages", locale);

		if(userRepository.existsByUserName(signupRequest.getUserName())){
			return ResponseEntity.badRequest().body(messages.getString("user.already-exists"));
		}
		if(userRepository.existsByEmail(signupRequest.getEmail())){
			return ResponseEntity.badRequest().body(messages.getString("email.already-exists"));
		}
		User user = new User();
		Set<Role> roles = new HashSet<>();
		user.setUserName(signupRequest.getUserName());
		user.setEmail(signupRequest.getEmail());
		user.setPassword(encoder.encode(signupRequest.getPassword()));
		log.info("User password encoded: " + user.getPassword());
		String[] roleArr = signupRequest.getRoles();

		if(roleArr == null) {
			roles.add(roleRepository.findByRoleName(Roles.ROLE_USER).get());
		}
		for(String role: roleArr) {
			switch(role.toLowerCase()) {
				case "admin":
					roles.add(roleRepository.findByRoleName(Roles.ROLE_ADMIN).get());
					break;
				case "user":
					roles.add(roleRepository.findByRoleName(Roles.ROLE_USER).get());
					break;
				default:
					return ResponseEntity.badRequest().body(messages.getString("role.not-found"));
			}
		}
		user.setRoles(roles);
		userRepository.save(user);
		return ResponseEntity.ok(messages.getString("user.login-success"));
	}

}
