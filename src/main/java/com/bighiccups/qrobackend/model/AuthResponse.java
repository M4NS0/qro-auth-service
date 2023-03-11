package com.bighiccups.qrobackend.model;

import lombok.Getter;
import lombok.Setter;

import java.util.List;
@Getter
@Setter
public class AuthResponse {
	private String token;
	private List<String> roles;

}
