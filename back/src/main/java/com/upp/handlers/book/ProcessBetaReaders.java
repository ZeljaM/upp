package com.upp.handlers.book;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


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
public class ProcessBetaReaders implements JavaDelegate
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
        String usersString = ( String ) execution.getVariable( "betaReadersBook" );
        String bookId = ( String ) execution.getVariable( "bookId" );

        Book book = this.IBookRepository.findById( Long.parseLong( bookId ) ).get();

        String[] split = usersString.split( ";" );

        List< User > users = new ArrayList<>();

        for ( String s : split )
        {
            Optional< User > findByEmail = this.iUserRepository.findByEmail( s );
            if ( findByEmail.isPresent() )
            {
                User user = findByEmail.get();
                users.add( user );
                this.EmailService.sendMessage( user.getEmail(), "Comment book", "You have to make comment to book with title [ " + book.getTitle() + " ]" );
            }
        }

        List< String > collect = users.stream().map( u -> u.getId().toString() ).collect( Collectors.toList() );

        execution.setVariable( "betaReaders", collect );

    }

}
