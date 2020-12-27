package com.upp.security.dtos;

import com.upp.models.RoleName;


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

    private RoleName role;

    public JwtAuthenticationResponse( String token, RoleName role )
    {
        this.accessToken = token;
        this.role = role;

    }

}
