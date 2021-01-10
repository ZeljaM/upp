package com.upp.handlers.book;

import com.upp.models.User;
import com.upp.repositories.IUserRepository;


import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class GiveReadersPoint implements JavaDelegate
{

    @Autowired
    private IUserRepository IUserRepository;

    @Override
    public void execute( DelegateExecution execution ) throws Exception
    {
        String oneReader = ( String ) execution.getVariable( "oneReader" );

        User user = this.IUserRepository.findById( Long.parseLong( oneReader ) ).get();

        System.err.println( user );

    }

}
