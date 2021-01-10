package com.upp.controllers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


import com.upp.configuration.UrlStorage;
import com.upp.dtos.ApiResponse;
import com.upp.dtos.FormFields;
import com.upp.dtos.PostFormRequest;
import com.upp.models.Book;
import com.upp.models.Genre;
import com.upp.models.RoleName;
import com.upp.models.User;
import com.upp.repositories.IBookRepository;
import com.upp.repositories.IGenreRepository;
import com.upp.repositories.IUserRepository;
import com.upp.security.JWTUtil;


import org.camunda.bpm.engine.FormService;
import org.camunda.bpm.engine.RuntimeService;
import org.camunda.bpm.engine.TaskService;
import org.camunda.bpm.engine.form.FormField;
import org.camunda.bpm.engine.form.TaskFormData;
import org.camunda.bpm.engine.runtime.ProcessInstance;
import org.camunda.bpm.engine.task.Task;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping( value = "/api/book" )
public class BookController
{

    @Autowired
    private IBookRepository iBookRepository;

    @Autowired
    JWTUtil jwtUtil;

    @Autowired
    private TaskService taskService;

    @Autowired
    private FormService formService;

    @Autowired
    private RuntimeService runtimeService;

    @Autowired
    private IUserRepository iUserRepository;

    @Autowired
    private IGenreRepository iGenreRepository;

    @GetMapping( value = "/process" )
    public ResponseEntity< FormFields > startProcess( @RequestHeader( required = true, value = "Authorization" ) final String token )
    {

        ProcessInstance instance = runtimeService.startProcessInstanceByKey( "bookProcess" );
        Task firstTask = this.taskService.createTaskQuery().processInstanceId( instance.getId() ).list().get( 0 );

        this.runtimeService.setVariable( instance.getId(), "betaComments", new ArrayList<>() );

        Long userId = this.jwtUtil.extractId( token );
        this.runtimeService.setVariable( instance.getId(), "userId", userId.toString() );

        User moderator = this.iUserRepository.findByRolesName( RoleName.ROLE_MODERATOR ).get( 0 );
        User lector = this.iUserRepository.findByRolesName( RoleName.ROLE_LECTOR ).get( 0 );

        this.runtimeService.setVariable( instance.getId(), "moderator", moderator.getId().toString() );
        this.runtimeService.setVariable( instance.getId(), "lector", lector.getId().toString() );

        List< User > findByRolesName = this.iUserRepository.findByRolesName( RoleName.ROLE_BETA );

        String beta = findByRolesName.stream().map( u -> u.getEmail() ).collect( Collectors.joining( ";" ) );

        this.runtimeService.setVariable( instance.getId(), "allBetaReaders", beta );
        List< Genre > allGenres = this.iGenreRepository.findAll();
        String defaultValuesForGenres = allGenres.stream().map( g -> g.getName() ).collect( Collectors.joining( ";" ) );

        this.runtimeService.setVariable( instance.getId(), "defaultValues", defaultValuesForGenres );

        TaskFormData taskFormData = this.formService.getTaskFormData( firstTask.getId() );
        String formKey = taskFormData.getFormKey();

        List< FormField > formFields = taskFormData.getFormFields();

        FormFields returnFormFields = new FormFields( firstTask.getId(), instance.getId(), formFields, new HashMap< String, String >(), formKey,
                UrlStorage.HOST + UrlStorage.BOOK_TASK );

        return new ResponseEntity< FormFields >( returnFormFields, HttpStatus.OK );

    }


    @PostMapping( value = "/task" )
    public ResponseEntity< ? > postForm( @RequestBody PostFormRequest form )
    {

        final Map< String, Object > map = new HashMap< String, Object >();

        map.putAll( form.getFields() );

        this.formService.submitTaskForm( form.getTask(), map );

        List< Task > tasks = this.taskService.createTaskQuery().processInstanceId( form.getProcess() ).list();

        if ( tasks.size() == 0 )
        {
            return new ResponseEntity< ApiResponse >( new ApiResponse( "Finished process", true ), HttpStatus.CREATED );
        }

        return new ResponseEntity< ApiResponse >( new ApiResponse( "Finished task", true ), HttpStatus.OK );

    }


    @PostMapping( "/file" )
    public ResponseEntity< ApiResponse > upload( @RequestParam( "file" ) final MultipartFile file,
            @RequestHeader( required = true, value = "Authorization" ) final String token, @RequestParam final String task, @RequestParam final String process )
    {

        try
        {
            Task singleResult = this.taskService.createTaskQuery().taskId( task ).singleResult();

            String bookId = ( String ) this.runtimeService.getVariable( process, "bookId" );

            Book book = this.iBookRepository.findById( Long.parseLong( bookId ) ).get();

            book.setBook( file.getBytes() );

            this.iBookRepository.save( book );

            this.runtimeService.setVariable( process, "comments", new ArrayList< String >() );
            this.runtimeService.setVariable( process, "comment", "" );
            this.runtimeService.setVariable( process, "moderatorComment", "" );
            this.runtimeService.setVariable( process, "lectorComment", "" );
            this.runtimeService.setVariable( process, "moderatorsReview", "" );
            this.taskService.complete( task );

            return new ResponseEntity< ApiResponse >( new ApiResponse( "Finished task", true ), HttpStatus.OK );
        }
        catch ( Exception e )
        {
            System.err.println( e );
            return new ResponseEntity< ApiResponse >( new ApiResponse( "Error", false ), HttpStatus.BAD_REQUEST );
        }

    }

}
