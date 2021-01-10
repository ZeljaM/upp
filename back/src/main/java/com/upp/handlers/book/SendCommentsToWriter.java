package com.upp.handlers.book;

import java.util.ArrayList;


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
@SuppressWarnings( value = "unchecked" )
public class SendCommentsToWriter implements JavaDelegate
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
        String bookId = ( String ) execution.getVariable( "bookId" );
        String userId = ( String ) execution.getVariable( "userId" );

        User user = this.iUserRepository.findById( Long.parseLong( userId ) ).get();

        Book book = this.IBookRepository.findById( Long.parseLong( bookId ) ).get();

        ArrayList< String > list = ( ArrayList< String > ) execution.getVariable( "betaComments" );

        this.EmailService.sendMessage( user.getEmail(), "Beta readers comments",
                "Beta readers comments for book with title [" + book.getTitle() + "] are " + list.toString() );

    }

}
