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
public class NotifyLector implements JavaDelegate
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
        String lecotrId = ( String ) execution.getVariable( "lector" );
        String bookId = ( String ) execution.getVariable( "bookId" );

        User lector = this.iUserRepository.findById( Long.parseLong( lecotrId ) ).get();
        Book book = this.IBookRepository.findById( Long.parseLong( bookId ) ).get();

        this.EmailService.sendMessage( lector.getEmail(), "Writer has uploaded new version of book",
                "Writer has uploaded new version of book with title [ " + book.getTitle() + " ]" );

    }

}
