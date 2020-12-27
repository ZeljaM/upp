package com.upp.handlers;

import java.util.Optional;


import com.upp.models.User;
import com.upp.repositories.IUserRepository;


import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ProcessGenresBeta implements JavaDelegate
{

    @Autowired
    private IUserRepository iUserRepository;

    @Override
    public void execute( DelegateExecution execution ) throws Exception
    {
        // TODO Auto-generated method stub
        String email = ( String ) execution.getVariable( "email" );

        execution.setVariable( "completed", true );

        Optional< User > findByEmail = this.iUserRepository.findByEmail( email );
        if ( findByEmail.isPresent() )
        {
            // TODO add beta genres
        }

    }

}
