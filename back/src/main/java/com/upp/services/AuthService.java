package com.upp.services;

import com.upp.security.dtos.JwtAuthenticationResponse;
import com.upp.security.dtos.LoginRequest;


import org.springframework.http.ResponseEntity;


public interface AuthService
{
    public ResponseEntity< JwtAuthenticationResponse > signIn( LoginRequest loginRequest );
    

}
