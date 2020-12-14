package com.upp.handlers;

import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.stereotype.Service;

@Service
public class ValidateUserRegistrationForm implements JavaDelegate
{

    @Override
    public void execute( DelegateExecution execution ) throws Exception
    {
        System.err.println( execution );

    }

}
