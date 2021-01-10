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
public class CheckForPlagiarism implements JavaDelegate
{

    @Autowired
    private IUserRepository iUserRepository;

    @Autowired
    private IBookRepository iBookRepository;

    @Autowired
    private EmailService EmailService;

    @Override
    public void execute( DelegateExecution execution ) throws Exception
    {
        String moderatorId = ( String ) execution.getVariable( "moderator" );

        User moderator = this.iUserRepository.findById( Long.parseLong( moderatorId ) ).get();

        String bookId = ( String ) execution.getVariable( "bookId" );
        Book book = this.iBookRepository.findById( Long.parseLong( bookId ) ).get();

        // this.EmailService.sendMessage( moderator.getEmail(), "Review uploaded book
        // file", "Review and read book with title [ " + book.getTitle() + " ]" );
        this.EmailService.sendMessage( moderator.getEmail(), "Decide if the book is plagiarism",
                "Decide if the book  book with title <b>[ " + book.getTitle() + " ]</b> is plagiarism" );

    }

}
