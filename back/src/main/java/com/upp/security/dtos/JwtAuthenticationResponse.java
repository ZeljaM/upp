package com.upp.security.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class JwtAuthenticationResponse
{

    private String accessToken;

    private String tokenType = "Bearer";

    public JwtAuthenticationResponse( String token )
    {
        this.accessToken = token;

    }

}
