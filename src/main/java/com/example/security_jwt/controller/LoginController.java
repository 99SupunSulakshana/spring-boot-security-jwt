package com.example.security_jwt.controller;


import com.example.security_jwt.payload.LoginRequest;
import com.example.security_jwt.payload.LoginResponse;
import com.example.security_jwt.service.JwtService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/v1/authentication")
public class LoginController {

    @Autowired
    private JwtService jwtService;

    @PostMapping("/login")
    public LoginResponse createJwtTokenAndLogin(@RequestBody LoginRequest loginRequest) throws Exception{
        return jwtService.createJwtToken(loginRequest);
    }
}
