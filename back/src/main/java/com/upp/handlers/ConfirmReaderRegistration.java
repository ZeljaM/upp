package com.upp.handlers;

import com.upp.dtos.PostFormRequest;
import com.upp.models.User;
import com.upp.repositories.IUserRepository;


import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ConfirmReaderRegistration implements JavaDelegate
{

    @Autowired
    private IUserRepository iUserRepository;

    @Override
    public void execute( DelegateExecution execution ) throws Exception
    {

        PostFormRequest form = ( PostFormRequest ) execution.getVariable( "form" );

        String code = form.getFields().get( "code" );

        String email = form.getFields().get( "email" );

        User user = this.iUserRepository.findByEmail( email ).get();

        if ( !user.getActivationCode().equals( code ) )
        {
            execution.setVariable( "codeValid", false );
            return;
        }

        execution.setVariable( "codeValid", true );

        user.setActive( true );

        this.iUserRepository.save( user );

    }

}
