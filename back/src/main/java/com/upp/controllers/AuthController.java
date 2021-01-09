package com.upp.controllers;

import com.upp.dtos.BASE64DecodedMultipartFile;
import com.upp.dtos.PaperDTO;
import com.upp.dtos.PlagiatorResponseDTO;
import com.upp.repositories.IBookRepository;
import com.upp.security.dtos.JwtAuthenticationResponse;
import com.upp.security.dtos.LoginRequest;
import com.upp.services.AuthService;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@RestController
@RequestMapping( "/api/auth" )
public class AuthController
{

    @Autowired
    private AuthService service;

    @Autowired
    private IBookRepository IBookRepository;

    @PostMapping( value = "/sign-in" )
    public ResponseEntity< JwtAuthenticationResponse > signIn( @RequestBody LoginRequest loginRequest )
    {
        return this.service.signIn( loginRequest );

    }


    @GetMapping( "/alo" )
    public String bla()
    {
        RestTemplate restTemplate = new RestTemplate();

        byte[] pdfFile = IBookRepository.findById( 1l ).get().getBook();
        BASE64DecodedMultipartFile base64DecodedMultipartFile = new BASE64DecodedMultipartFile( pdfFile );

        PaperDTO paperDTO = new PaperDTO();

        paperDTO.setFile( base64DecodedMultipartFile );

        ResponseEntity< PlagiatorResponseDTO > postForEntity =
                restTemplate.postForEntity( "http://localhost:8000/api/file/upload/new", paperDTO, PlagiatorResponseDTO.class );

        System.err.println( postForEntity );

        return "";

    }

}
