package com.upp.handlers;

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

    @Override
    public void execute( DelegateExecution execution ) throws Exception
    {

        String email = ( String ) execution.getVariable( "email" );

        this.EmailService.sendMessage( email, "Your registration has run out of time", "Your registration has run out of time" );

    }

}
