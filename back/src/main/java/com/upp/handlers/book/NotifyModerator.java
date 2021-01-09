package com.upp.handlers.book;

import java.util.Map;
import java.util.Optional;


import com.upp.models.Book;
import com.upp.models.Genre;
import com.upp.models.User;
import com.upp.repositories.IBookRepository;
import com.upp.repositories.IGenreRepository;
import com.upp.repositories.IUserRepository;
import com.upp.services.EmailService;


import org.camunda.bpm.engine.FormService;
import org.camunda.bpm.engine.TaskService;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.camunda.bpm.engine.form.TaskFormData;
import org.camunda.bpm.engine.task.Task;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class NotifyModerator implements JavaDelegate
{

    @Autowired
    private EmailService EmailService;

    @Autowired
    private IUserRepository iUserRepository;

    @Autowired
    private IGenreRepository iGenreRepository;

    @Autowired
    private IBookRepository iBookRepository;

    @Autowired
    private FormService formService;

    @Autowired
    private TaskService taskService;

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

        Task currentTask = this.taskService.createTaskQuery().processInstanceId( execution.getProcessInstanceId() ).singleResult();
        TaskFormData taskFormData = this.formService.getTaskFormData( currentTask.getId() );

        Map< String, String > map = new java.util.HashMap< String, String >();

        taskFormData.getFormFields().forEach( ff ->
        {

            map.put( ff.getId(), ff.getValue().getValue().toString() );
        } );

        Book newBook = new Book( map );

        String genreString = map.get( "genre" );

        Optional< Genre > findByName = this.iGenreRepository.findByName( genreString );

        if ( findByName.isPresent() )
        {
            newBook.setGenre( findByName.get() );
        }

        Book save = this.iBookRepository.save( newBook );

        String userId = ( String ) execution.getVariable( "userId" );

        User user = this.iUserRepository.findById( Long.parseLong( userId ) ).get();

        user.getBooks().add( save );

        execution.setVariable( "bookId", save.getId().toString() );

    }

}
