package com.upp.controllers;

import com.upp.security.dtos.JwtAuthenticationResponse;
import com.upp.security.dtos.LoginRequest;
import com.upp.services.AuthService;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping( "/api/auth" )
public class AuthController
{

    @Autowired
    private AuthService service;


    @PostMapping( value = "/sign-in" )
    public ResponseEntity< JwtAuthenticationResponse > signIn( @RequestBody LoginRequest loginRequest )
    {
        return this.service.signIn( loginRequest );

    }


   

}
