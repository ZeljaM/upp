package com.upp.handlers;

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

    }

}
