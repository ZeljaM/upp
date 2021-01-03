package com.upp.handlers.plagiarism;

import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.stereotype.Service;

@Service
public class CountVotes implements JavaDelegate
{

    @Override
    public void execute( DelegateExecution execution ) throws Exception
    {
        // TODO Auto-generated method stub

    }

}
