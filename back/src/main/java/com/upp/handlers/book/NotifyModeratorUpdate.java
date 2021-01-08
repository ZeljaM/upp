package com.upp.handlers.book;

import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.jvnet.hk2.annotations.Service;

@Service
public class NotifyModeratorUpdate implements JavaDelegate
{

    @Override
    public void execute( DelegateExecution execution ) throws Exception
    {
        // TODO Auto-generated method stub

    }

}
