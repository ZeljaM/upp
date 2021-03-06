package com.upp.security;

import java.util.function.Function;


import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;

public class JWTUtil
{

    private String SECRET_KEY = "JWTSuperSecretKey";

    public < T > T extractClaim( String token, Function< Claims, T > claimsResolver )
    {
        final Claims claims = extractAllClaims( token );
        return claimsResolver.apply( claims );

    }


    private Claims extractAllClaims( String token )
    {
        return Jwts.parser().setSigningKey( SECRET_KEY ).parseClaimsJws( token ).getBody();

    }


    public Long extractId( String token )
    {
        return Long.valueOf( extractClaim( token.substring( 7 ), Claims::getSubject ) );

    }

}
