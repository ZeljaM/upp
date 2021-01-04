package com.upp.handlers.plagiarism;

import java.util.Optional;


import com.upp.models.User;
import com.upp.repositories.IUserRepository;
import com.upp.services.EmailService;


import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UpdateBook implements JavaDelegate
{

    @Autowired
    private IUserRepository iUserRepository;

    @Autowired
    private EmailService emailService;

    @Override
    public void execute( DelegateExecution execution ) throws Exception
    {
        String userId = ( String ) execution.getVariable( "userId" );

        Optional< User > findById = this.iUserRepository.findById( Long.parseLong( userId ) );

        if ( findById.isPresent() )
        {
            User user = findById.get();

            Boolean plagiarism = ( Boolean ) execution.getVariable( "isPlagiarism" );

            String message =
                    plagiarism ? "Your appeal has been rejected! Your book is plagiarism!" : "You appeal has been accepted! Your book is not plagiarism!";

            this.emailService.sendMessage( user.getEmail(), message, message );
        }

    }

}
