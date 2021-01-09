package com.upp.handlers.book;

import com.upp.models.User;
import com.upp.repositories.IUserRepository;
import com.upp.services.EmailService;


import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class NotifyWriterUploadFile implements JavaDelegate
{

    @Autowired
    private IUserRepository iUserRepository;

    @Autowired
    private EmailService EmailService;

    @Override
    public void execute( DelegateExecution execution ) throws Exception
    {
        String userId = ( String ) execution.getVariable( "userId" );

        User user = this.iUserRepository.findById( Long.parseLong( userId ) ).get();

        this.EmailService.sendMessage( user.getEmail(), "Upload file", "You can now upload file for review before publishing" );

    }

}
