package com.upp.handlers.writer;

import com.upp.models.User;
import com.upp.repositories.IUserRepository;
import com.upp.services.EmailService;


import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class FourTimesVote implements JavaDelegate
{

    @Autowired
    private IUserRepository iUserRepository;

    @Autowired
    private EmailService EmailService;

    @Override
    public void execute( DelegateExecution execution ) throws Exception
    {
        String email = ( String ) execution.getVariable( "email" );

        User user = this.iUserRepository.findByEmail( email ).get();

        user.setActive( false );

        this.iUserRepository.save( user );

        EmailService.sendMessage( email, "Registration cancelled", "Registration has been cancelled! Four times vote is allowed!" );

    }

}
