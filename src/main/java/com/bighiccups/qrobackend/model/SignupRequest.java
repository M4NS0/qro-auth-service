package com.bighiccups.qrobackend.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SignupRequest {
	private String userName; 
	private String email;
	private String password;
	private String[] roles;

}
