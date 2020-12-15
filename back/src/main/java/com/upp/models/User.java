package com.upp.models;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;


import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table( name = "model_user", uniqueConstraints =
{ @UniqueConstraint( columnNames =
{ "username" } ), @UniqueConstraint( columnNames =
{ "email" } ) } )
public class User
{

    @Id
    @GeneratedValue( strategy = GenerationType.IDENTITY )
    private Long id;

    private String email;

    private String username;

    private String password;

    private String city;

    private String country;

    private String firstName;

    private String lastName;

    private String activationCode;

    // ! not like alpha and beta male :D
    private Boolean beta;

    @ManyToMany( fetch = FetchType.LAZY )
    @JoinTable( name = "model_user_genres", joinColumns = @JoinColumn( name = "user_id" ), inverseJoinColumns = @JoinColumn( name = "genre_id" ) )
    private Set< Genre > genres = new HashSet<>();

    @ManyToMany( fetch = FetchType.LAZY )
    @JoinTable( name = "model_user_beta_genres", joinColumns = @JoinColumn( name = "user_id" ), inverseJoinColumns = @JoinColumn( name = "genre_id" ) )
    private Set< Genre > betaGenres = new HashSet<>();

    @ManyToMany( fetch = FetchType.LAZY )
    @JoinTable( name = "model_user_roles", joinColumns = @JoinColumn( name = "user_id" ), inverseJoinColumns = @JoinColumn( name = "role_id" ) )
    private Set< Role > roles = new HashSet< Role >();

    private Boolean active;

    public User( Map< String, String > map )
    {
        this.email = map.get( "email" );
        this.username = map.get( "username" );
        this.password = map.get( "password" );
        this.city = map.get( "city" );
        this.country = map.get( "country" );
        this.firstName = map.get( "firstname" );
        this.lastName = map.get( "lastname" );
        this.beta = Boolean.parseBoolean( map.get( "beta" ) );
        this.active = false;

    }

}
