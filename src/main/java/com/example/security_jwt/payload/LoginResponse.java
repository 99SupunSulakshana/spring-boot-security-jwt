package com.example.security_jwt.payload;

import com.example.security_jwt.entity.User;

public class LoginResponse {
    private User user;
    private String jwtToken;

    public LoginResponse(User user, String newGeneratedToken) {
        this.user = user;
        this.jwtToken = newGeneratedToken;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getJwtToken() {
        return jwtToken;
    }

    public void setJwtToken(String jwtToken) {
        this.jwtToken = jwtToken;
    }
}
