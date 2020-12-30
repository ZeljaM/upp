package com.upp.handlers;

import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.stereotype.Service;

@Service
public class DummyTaskAgain implements JavaDelegate
{

    @Override
    public void execute( DelegateExecution execution ) throws Exception
    {
        // TODO Auto-generated method stub
        System.err.println( "END" );

        String email = ( String ) execution.getVariable( "email" );

        System.err.println( email );

    }

}
