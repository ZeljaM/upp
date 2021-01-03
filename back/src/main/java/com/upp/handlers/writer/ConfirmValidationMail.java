package com.upp.handlers.writer;

import com.upp.models.User;
import com.upp.repositories.IUserRepository;
import com.upp.services.EmailService;


import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ConfirmValidationMail implements JavaDelegate
{

    @Autowired
    private IUserRepository iUserRepository;

    @Autowired
    private EmailService emailService;

    @Override
    public void execute( DelegateExecution execution ) throws Exception
    {

        String email = ( String ) execution.getVariable( "email" );

        User user = this.iUserRepository.findByEmail( email ).get();

        user.setActive( true );

        this.iUserRepository.save( user );

        String text =
                "<!DOCTYPE html>\r\n<html lang=\"en\">\r\n\r\n<head>\r\n    <meta charset=\"UTF-8\">\r\n    <meta name=\"viewport\"\r\n          content=\"width=device-width, initial-scale=1.0\">\r\n    <title>Verify your account</title>\r\n    <style>\r\n        body {\r\n            background-color: aquamarine;\r\n            color: hotpink;\r\n        }\r\n\r\n        h1,\r\n        h2,\r\n        h3 {\r\n            text-align: center;\r\n        }\r\n\r\n        b {\r\n            color: red;\r\n        }\r\n    </style>\r\n</head>\r\n\r\n<body>\r\n    <h1>Upload files</h1>\r\n    <h2>Greetings "
                        + user.getFirstName() + " " + user.getLastName() + " </h2>\r\n    <h3>Upload files!</h3>\r\n</body>\r\n\r\n</html>\r\n";

        emailService.sendMessage( email, "Verified", text );

        execution.removeVariable( "verified" );
        execution.setVariable( "verified", true );

    }

}
