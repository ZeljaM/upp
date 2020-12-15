package com.upp.services;

import javax.mail.internet.MimeMessage;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
public class EmailService
{

    @Autowired
    private JavaMailSender javaMailSender;

    @Autowired
    private Environment environment;

    public void sendMessage( final String to, final String subject, final String message )
    {
        try
        {
            MimeMessage mimeMessage = javaMailSender.createMimeMessage();

            mimeMessage.setSubject( subject );

            MimeMessageHelper helper = new MimeMessageHelper( mimeMessage, true );

            helper.setFrom( environment.getProperty( "spring.mail.username" ) );
            helper.setTo( to );
            helper.setText( message, true );
            javaMailSender.send( mimeMessage );

        }
        catch ( Exception e )
        {
            System.err.println( e.getMessage() );
        }

    }

}
