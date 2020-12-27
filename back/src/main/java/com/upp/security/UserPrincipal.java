package com.upp.security;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.upp.models.User;


import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;


import lombok.Data;

@Data
public class UserPrincipal implements UserDetails
{

    /**
     *
     */
    private static final long serialVersionUID = 8392350135980097758L;

    private Long id;

    private String email;

    private String username;

    @JsonIgnore
    private String password;

    private String city;

    private String country;

    private String firstName;

    private String lastName;

    private Boolean beta;

    private Boolean active;

    private Collection< ? extends GrantedAuthority > authorities;

    public UserPrincipal( Long id, String email, String username, String password, String city, String country, String firstName, String lastName, Boolean beta,
            Collection< ? extends GrantedAuthority > authorities, Boolean active )
    {
        super();
        this.id = id;
        this.email = email;
        this.username = username;
        this.password = password;
        this.city = city;
        this.country = country;
        this.firstName = firstName;
        this.lastName = lastName;
        this.beta = beta;
        this.authorities = authorities;
        this.active = active;

    }


    public static UserPrincipal create( User user )
    {
        List< GrantedAuthority > authorities =
                user.getRoles().stream().map( role -> new SimpleGrantedAuthority( role.getName().name() ) ).collect( Collectors.toList() );

        return new UserPrincipal( user.getId(), user.getEmail(), user.getUsername(), user.getPassword(), user.getCity(), user.getCountry(), user.getFirstName(),
                user.getLastName(), user.getBeta(), authorities, user.getActive() );

    }


    @Override
    public Collection< ? extends GrantedAuthority > getAuthorities()
    {

        return this.authorities;

    }


    @Override
    public String getPassword()
    {

        return this.password;

    }


    @Override
    public String getUsername()
    {

        return this.username;

    }


    @Override
    public boolean isAccountNonExpired()
    {

        return true;

    }


    @Override
    public boolean isAccountNonLocked()
    {

        return true;

    }


    @Override
    public boolean isCredentialsNonExpired()
    {

        return true;

    }


    @Override
    public boolean isEnabled()
    {

        return this.active;

    }

}
