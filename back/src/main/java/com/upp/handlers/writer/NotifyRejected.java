package com.upp.handlers.writer;

import com.upp.models.User;
import com.upp.repositories.IUserRepository;
import com.upp.services.EmailService;


import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class NotifyRejected implements JavaDelegate
{

    @Autowired
    private EmailService emailService;

    @Autowired
    private IUserRepository IUserRepository;

    @Override
    public void execute( DelegateExecution execution ) throws Exception
    {

        String email = ( String ) execution.getVariable( "email" );

        User user = this.IUserRepository.findByEmail( email ).get();

        user.setActive( false );

        this.IUserRepository.save( user );

        emailService.sendMessage( email, "Your registration has been rejected", "Your registration has been rejected!!!" );

    }

}
