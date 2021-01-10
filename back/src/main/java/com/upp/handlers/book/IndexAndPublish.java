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
public class IndexAndPublish implements JavaDelegate
{

    @Autowired
    private IBookRepository iBookRepository;

    @Autowired
    private IUserRepository iUserRepository;

    @Autowired
    private EmailService EmailService;

    @Override
    public void execute( DelegateExecution execution ) throws Exception
    {
        String bookId = ( String ) execution.getVariable( "bookId" );

        String userId = ( String ) execution.getVariable( "userId" );

        User user = this.iUserRepository.findById( Long.parseLong( userId ) ).get();
        Book book = this.iBookRepository.findById( Long.parseLong( bookId ) ).get();

        this.EmailService.sendMessage( user.getEmail(), "Your book [ " + book.getTitle() + " ] has been published",
                "Your book [ " + book.getTitle() + " | " + book.getSynopsis() + " ] has been published" );

        book.setPublished( true );

        this.iBookRepository.save( book );

    }

}
