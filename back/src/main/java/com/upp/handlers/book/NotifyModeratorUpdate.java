package com.upp.handlers.book;

import com.upp.models.Book;
import com.upp.models.User;
import com.upp.repositories.IBookRepository;
import com.upp.repositories.IUserRepository;
import com.upp.services.EmailService;


import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class NotifyModeratorUpdate implements JavaDelegate
{

    @Autowired
    private IUserRepository iUserRepository;

    @Autowired
    private EmailService EmailService;

    @Autowired
    private IBookRepository IBookRepository;

    @Override
    public void execute( DelegateExecution execution ) throws Exception
    {
        String moderatorId = ( String ) execution.getVariable( "moderator" );
        String bookId = ( String ) execution.getVariable( "bookId" );

        User moderator = this.iUserRepository.findById( Long.parseLong( moderatorId ) ).get();
        Book book = this.IBookRepository.findById( Long.parseLong( bookId ) ).get();

        this.EmailService.sendMessage( moderator.getEmail(), "Writer has uploaded new version of book",
                "Writer has uploaded new version of book with title [ " + book.getTitle() + " ]" );

    }

}
