package com.upp.handlers;

import com.upp.services.EmailService;


import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.beans.factory.annotation.Autowired;

public class CancelReaderRegistration implements JavaDelegate
{


    @Autowired
    EmailService EmailService;

    @Override
    public void execute( DelegateExecution execution ) throws Exception
    {

        System.err.println( "CANCEL" );

        EmailService.sendMessage("zeljkom96@gmail.com", "OK", "message");

    }

}
