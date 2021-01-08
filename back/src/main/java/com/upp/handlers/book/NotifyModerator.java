package com.upp.handlers.book;

import java.util.Optional;


import com.upp.models.User;
import com.upp.repositories.IUserRepository;
import com.upp.services.EmailService;


import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class NotifyModerator implements JavaDelegate
{

    @Autowired
    private EmailService EmailService;

    @Autowired
    private IUserRepository iUserRepository;

    @Override
    public void execute( DelegateExecution execution ) throws Exception
    {

        String moderatorId = ( String ) execution.getVariable( "moderator" );

        Optional< User > findById = this.iUserRepository.findById( Long.parseLong( moderatorId ) );

        if ( findById.isPresent() )
        {
            User moderator = findById.get();

            this.EmailService.sendMessage( moderator.getEmail(), "Publishing new book", "Writer has requested starting new book" );
        }

    }

}
