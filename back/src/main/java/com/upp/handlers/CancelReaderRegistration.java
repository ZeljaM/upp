package com.upp.handlers;

import java.util.Optional;


import com.upp.models.User;
import com.upp.repositories.IUserRepository;
import com.upp.services.EmailService;


import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CancelReaderRegistration implements JavaDelegate
{

    @Autowired
    EmailService EmailService;

    @Autowired
    private IUserRepository iUserService;

    @Override
    public void execute( DelegateExecution execution ) throws Exception
    {

        System.err.println( "CANCEL" );

        String email = ( String ) execution.getVariable( "email" );

        Optional< User > findByEmail = this.iUserService.findByEmail( email );
        if ( findByEmail.isPresent() )
        {
            this.iUserService.delete( findByEmail.get() );
        }

        EmailService.sendMessage( email, "Registration is expired", "Registration is expired" );

    }

}
