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
public class NotifyWriterModeratorReview implements JavaDelegate
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
        String comment = ( String ) execution.getVariable( "moderatorsReview" );

        String userId = ( String ) execution.getVariable( "userId" );
        String bookId = ( String ) execution.getVariable( "bookId" );

        Book book = this.IBookRepository.findById( Long.parseLong( bookId ) ).get();
        User user = this.iUserRepository.findById( Long.parseLong( userId ) ).get();

        this.EmailService.sendMessage( user.getEmail(), "Upload revision of file",
                "Upload revision of file for book [ " + book.getTitle() + " ]; Comment is: " + comment );

    }

}
