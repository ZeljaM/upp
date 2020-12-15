package com.upp.security;

import javax.transaction.Transactional;


import com.upp.models.User;
import com.upp.repositories.IUserRepository;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class CustomUserDetailsService implements UserDetailsService
{

    @Autowired
    private IUserRepository iUserRepository;

    @Override
    @Transactional
    public UserDetails loadUserByUsername( String username ) throws UsernameNotFoundException
    {

        User user = this.iUserRepository.findByUsernameOrEmail( username, username )
                .orElseThrow( () -> new UsernameNotFoundException( "User not found with username or email : " + username ) );
        return UserPrincipal.create( user );

    }


    @Transactional
    public UserDetails loadUserById( Long id )
    {
        User user = this.iUserRepository.findById( id ).orElseThrow( () -> new UsernameNotFoundException( "User not found with id : " + id ) );

        return UserPrincipal.create( user );

    }

}
