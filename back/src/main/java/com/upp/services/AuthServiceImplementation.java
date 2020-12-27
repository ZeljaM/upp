package com.upp.services;

import java.util.Set;


import com.upp.models.Role;
import com.upp.models.User;
import com.upp.repositories.IUserRepository;
import com.upp.security.JwtTokenProvider;
import com.upp.security.UserPrincipal;
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

    @Autowired
    private IUserRepository IUserRepository;

    @Override
    public ResponseEntity< JwtAuthenticationResponse > signIn( LoginRequest loginRequest )
    {
        Authentication authentication =
                authenticationManager.authenticate( new UsernamePasswordAuthenticationToken( loginRequest.getUsernameOrEmail(), loginRequest.getPassword() ) );

        SecurityContextHolder.getContext().setAuthentication( authentication );

        String jwt = tokenProvider.generateToken( authentication );
        UserPrincipal userPrincipal = ( UserPrincipal ) authentication.getPrincipal();
        Long id = userPrincipal.getId();

        User user = this.IUserRepository.findById( id ).get();

        Set< Role > roles = user.getRoles();
        Role role = roles.iterator().next();

        return ResponseEntity.ok( new JwtAuthenticationResponse( jwt, role.getName() ) );

    }

}
