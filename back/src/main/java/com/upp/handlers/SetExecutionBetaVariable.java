package com.upp.handlers;

import com.upp.dtos.PostFormRequest;


import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;

public class SetExecutionBetaVariable implements JavaDelegate
{

    @Override
    public void execute( DelegateExecution execution ) throws Exception
    {
        PostFormRequest form = ( PostFormRequest ) execution.getVariable( "form" );

        String string = form.getFields().get( "beta" );

        boolean parseBoolean = Boolean.parseBoolean( string );

        execution.setVariable( "beta", parseBoolean );

    }

}
