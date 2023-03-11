package com.bighiccups.qrobackend.model;

import lombok.Getter;

@Getter
public class JwtRequest {
    private String token;

    public String getToken() {
        return this.token;
    }
}
