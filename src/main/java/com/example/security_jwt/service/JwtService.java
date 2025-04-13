package com.example.security_jwt.service;

import com.example.security_jwt.entity.Role;
import com.example.security_jwt.entity.User;
import com.example.security_jwt.payload.LoginRequest;
import com.example.security_jwt.payload.LoginResponse;
import com.example.security_jwt.repo.UserRepo;
import com.example.security_jwt.util.JwtUtil;
import io.jsonwebtoken.security.JwkThumbprint;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

@Service
public class JwtService implements UserDetailsService {
    @Autowired
    private UserRepo userRepo;
    @Autowired
    private JwtUtil jwtUtil;

    private final AuthenticationConfiguration authenticationConfiguration;

    public JwtService(AuthenticationConfiguration authenticationConfiguration) {
        this.authenticationConfiguration = authenticationConfiguration;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepo.findById(username).orElse(null);
        if(user != null){
            return new org.springframework.security.core.userdetails.User(
                    user.getUserName(),
                    user.getUserPassword(),
                    getAuthority(user)
            );
        }else{
            throw new UsernameNotFoundException("User not found with username: "+username);
        }
    }

    private Set<SimpleGrantedAuthority> getAuthority(User user) {
        Set<SimpleGrantedAuthority> authoritySet = new HashSet<>();
        for(Role role: user.getRole()){
            authoritySet.add(new SimpleGrantedAuthority("ROLE_"+role.getRoleName()));
        }
        return authoritySet;
    }

    public LoginResponse createJwtToken(LoginRequest loginRequest) throws Exception {
        String username = loginRequest.getUsername();
        String userPassword = loginRequest.getPassword();
        authenticate(username, userPassword);

        UserDetails userDetails = loadUserByUsername(username);
        String newGeneratedToken = jwtUtil.generateToken(userDetails.getUsername());
        User user = userRepo.findById(username).get();

        return new LoginResponse(
                user,newGeneratedToken
        );
    }

    private void authenticate(String username, String userPassword) throws Exception {
        try{
            AuthenticationManager authenticationManager = authenticationConfiguration.getAuthenticationManager();
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, userPassword));
        }catch (BadCredentialsException e){
            throw new Exception("INVALID_CREDENTIALS", e);
        }
    }
}
