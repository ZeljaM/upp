package com.upp.handlers.writer;

import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.stereotype.Service;

@Service
public class ProcessVoting implements JavaDelegate
{

    @Override
    public void execute( DelegateExecution execution ) throws Exception
    {
        System.err.println( execution );
        System.err.println( execution );
        System.err.println( execution );
        System.err.println( execution );

    }

}
