package com.upp.handlers;

import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;

public class SetExecutionBetaVariable implements JavaDelegate
{

    @Override
    public void execute( DelegateExecution execution ) throws Exception
    {
        Boolean beta = ( Boolean ) execution.getVariable( "beta" );

        execution.setVariable( "beta", beta );

    }

}
