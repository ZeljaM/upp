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
import com.upp.models.RoleName;
import com.upp.models.User;
import com.upp.repositories.IBookRepository;
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
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping( value = "/register/writer" )
public class WriterRegistrationController
{

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
    private IBookRepository iBookRepository;

    @GetMapping( "/process" )
    public ResponseEntity< FormFields > startProcess()
    {
        ProcessInstance instance = runtimeService.startProcessInstanceByKey( "register_writer" );
        Task firstTask = this.taskService.createTaskQuery().processInstanceId( instance.getId() ).list().get( 0 );

        this.runtimeService.setVariable( instance.getId(), "verified", false );
        this.runtimeService.setVariable( instance.getId(), "notifyCount", 0 );
        this.runtimeService.setVariable( instance.getId(), "voteCount", 0 );
        this.runtimeService.setVariable( instance.getId(), "moreFiles", false );
        this.runtimeService.setVariable( instance.getId(), "voteAgain", false );
        this.runtimeService.setVariable( instance.getId(), "rejected", false );

        this.runtimeService.setVariable( instance.getId(), "votes", new ArrayList< String >() );

        List< User > findByRolesName = this.iUserRepository.findByRolesName( RoleName.ROLE_EDITOR );

        List< String > collect = findByRolesName.stream().map( u -> u.getId().toString() ).collect( Collectors.toList() );

        TaskFormData taskFormData = this.formService.getTaskFormData( firstTask.getId() );
        String formKey = taskFormData.getFormKey();

        List< FormField > formFields = taskFormData.getFormFields();

        FormFields returnFormFields = new FormFields( firstTask.getId(), instance.getId(), formFields, new HashMap< String, String >(), formKey,
                UrlStorage.HOST + UrlStorage.POST_WRITER );

        return new ResponseEntity< FormFields >( returnFormFields, HttpStatus.OK );

    }


    // public ResponseEntity< ? > postTask(@RequestParam( "files" ) final
    // MultipartFile[] files )
    @PostMapping( "/task" )
    @SuppressWarnings( "unchecked" )
    public ResponseEntity< ? > postTask( @RequestBody PostFormRequest form )
    {

        final Map< String, Object > map = new HashMap< String, Object >();

        map.putAll( form.getFields() );

        this.formService.submitTaskForm( form.getTask(), map );

        List< Task > tasks = this.taskService.createTaskQuery().processInstanceId( form.getProcess() ).list();

        Task nextTask = tasks.get( 0 );

        TaskFormData taskFormData = this.formService.getTaskFormData( nextTask.getId() );
        String formKey = taskFormData.getFormKey();
        List< FormField > formFields = taskFormData.getFormFields();

        Boolean verified = ( Boolean ) runtimeService.getVariable( form.getProcess(), "verified" );
        if ( verified )
        {
            String userId = ( String ) runtimeService.getVariable( form.getProcess(), "userId" );
            nextTask.setAssignee( userId );
            return new ResponseEntity< ApiResponse >( new ApiResponse( "You can now sign in", true ), HttpStatus.OK );
        }

        HashMap< String, String > errors = ( HashMap< String, String > ) runtimeService.getVariable( form.getProcess(), "errors" );
        if ( errors.isEmpty() )
        {
            return new ResponseEntity< ApiResponse >( new ApiResponse( "Verify account by email!", true ), HttpStatus.CREATED );
        }
        FormFields returnFormFields =
                new FormFields( nextTask.getId(), form.getProcess(), formFields, errors, formKey, UrlStorage.HOST + UrlStorage.POST_WRITER );

        return new ResponseEntity< FormFields >( returnFormFields, HttpStatus.OK );

    }


    @GetMapping( "/verify/{process}/{task}" )
    public ResponseEntity< ? > verify( @PathVariable String process, @PathVariable String task )
    {

        Task currentTask = taskService.createTaskQuery().processInstanceId( process ).singleResult();

        PostFormRequest postFormRequest = new PostFormRequest();

        postFormRequest.setProcess( process );
        postFormRequest.setTask( currentTask.getId() );

        RestTemplate restTemplate = new RestTemplate();

        ResponseEntity< Object > postForEntity = restTemplate.postForEntity( "http://localhost:8081/register/writer/task/", postFormRequest, Object.class );

        return postForEntity;

    }


    @PostMapping( "/files" )
    public ResponseEntity< ApiResponse > upload( @RequestParam( "files" ) final MultipartFile[] files,
            @RequestHeader( required = true, value = "Authorization" ) final String token, @RequestParam final String task, @RequestParam final String process )
    {

        Long extractId = this.jwtUtil.extractId( token );

        User user = this.iUserRepository.findById( extractId ).get();
        Task singleResult = this.taskService.createTaskQuery().taskDefinitionKey( task ).singleResult();
        final Map< String, Object > map = new HashMap< String, Object >();

        int length = files.length;
        this.runtimeService.setVariable( process, "filesMultiPart", files );

        try
        {
            for ( MultipartFile file : files )
            {
                Book newBook = new Book();

                newBook.setBook( file.getBytes() );
                this.iBookRepository.save( newBook );

                user.getBooks().add( newBook );

            }

            this.iUserRepository.save( user );
        }
        catch ( Exception e )
        {
            System.err.println( e.toString() );
        }

        this.formService.submitTaskForm( task, null );
        return new ResponseEntity< ApiResponse >( new ApiResponse( "Uploaded " + length + " files", true ), HttpStatus.OK );

    }

}
