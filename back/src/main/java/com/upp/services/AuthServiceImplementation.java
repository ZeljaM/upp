package com.upp.services;

import com.upp.security.JwtTokenProvider;
import com.upp.security.dtos.JwtAuthenticationResponse;
import com.upp.security.dtos.LoginRequest;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
public class AuthServiceImplementation implements AuthService
{

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtTokenProvider tokenProvider;

    @Override
    public ResponseEntity< JwtAuthenticationResponse > signIn( LoginRequest loginRequest )
    {
        Authentication authentication =
                authenticationManager.authenticate( new UsernamePasswordAuthenticationToken( loginRequest.getUsernameOrEmail(), loginRequest.getPassword() ) );

        SecurityContextHolder.getContext().setAuthentication( authentication );

        String jwt = tokenProvider.generateToken( authentication );
        return ResponseEntity.ok( new JwtAuthenticationResponse( jwt ) );

    }

}
