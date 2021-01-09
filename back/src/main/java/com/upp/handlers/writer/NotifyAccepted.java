package com.upp.handlers.writer;

import com.upp.services.EmailService;


import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class NotifyAccepted implements JavaDelegate
{

    @Autowired
    private EmailService emailService;

    

    @Override
    public void execute( DelegateExecution execution ) throws Exception
    {

        String email = ( String ) execution.getVariable( "email" );

        emailService.sendMessage( email, "Your registration has been accepted", "Your registration has been accepted!!! Log in and pay!" );

    }

}
