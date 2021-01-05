package com.upp;

import java.util.TimeZone;


import javax.annotation.PostConstruct;


import com.upp.security.JWTUtil;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;

@RestController
@SpringBootApplication
public class Application
{

  public static void main( String[] args )
  {
    SpringApplication.run( Application.class );

  }


  @PostConstruct
  public void init()
  {
    // Setting Spring Boot SetTimeZone
    TimeZone.setDefault( TimeZone.getTimeZone( "UTC+1" ) );

  }


  @Bean( name = "multipartResolver" )
  public CommonsMultipartResolver multipartResolver()
  {
    CommonsMultipartResolver multipartResolver = new CommonsMultipartResolver();
    multipartResolver.setMaxUploadSize( 100000000 );
    return multipartResolver;

  }


  @Bean
  public JWTUtil jwtUtilObject()
  {
    return new JWTUtil();

  }

}
