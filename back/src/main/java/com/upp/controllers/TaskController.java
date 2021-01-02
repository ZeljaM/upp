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
import com.upp.dtos.TaskInfo;
import com.upp.models.Role;
import com.upp.models.RoleName;
import com.upp.models.User;
import com.upp.repositories.IRoleRepository;
import com.upp.repositories.IUserRepository;
import com.upp.security.JWTUtil;


import org.camunda.bpm.engine.FormService;
import org.camunda.bpm.engine.RuntimeService;
import org.camunda.bpm.engine.TaskService;
import org.camunda.bpm.engine.form.FormField;
import org.camunda.bpm.engine.form.TaskFormData;
import org.camunda.bpm.engine.task.Task;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping( "/api/task" )
public class TaskController
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
    private IRoleRepository iRoleRepository;

    @GetMapping( "/" )
    public ResponseEntity< ? > next( @RequestHeader( required = true, value = "Authorization" ) final String token )
    {
        List< TaskInfo > tasks = new ArrayList< TaskInfo >();
        Long id = this.jwtUtil.extractId( token );

        List< Task > list = this.taskService.createTaskQuery().taskAssignee( id.toString() ).list();

        list.forEach( t ->
        {
            tasks.add( new TaskInfo( t.getProcessInstanceId(), t.getId(), t.getName(), t.getCreateTime(), null ) );
        } );

        return new ResponseEntity<>( tasks, HttpStatus.OK );

    }


    @PostMapping( "/form" )
    public ResponseEntity< ? > form( @RequestHeader( required = true, value = "Authorization" ) final String token, @RequestBody PostFormRequest form )
    {
        try
        {

            String userId = ( String ) this.runtimeService.getVariable( form.getProcess(), "userId" );

            long parseLong = Long.parseLong( userId );
            User user = this.iUserRepository.findById( parseLong ).get();

            TaskFormData taskFormData = this.formService.getTaskFormData( form.getTask() );
            String formKey = taskFormData.getFormKey();
            List< FormField > formFields = taskFormData.getFormFields();
            String url = UrlStorage.HOST;
            if ( taskFormData.getFormKey().equals( "files" ) )
            {
                url += UrlStorage.FILES;
            }
            else
            {
                url += UrlStorage.TASK;
            }

            FormFields returnFormFields = new FormFields( form.getTask(), form.getProcess(), formFields, new HashMap<>(), formKey, url );
            if ( taskFormData.getFormKey().equals( "voting" ) )
            {

                List< byte[] > collect = user.getBooks().stream().map( b -> b.getBook() ).collect( Collectors.toList() );

                returnFormFields.setFiles( collect );
            }

            return new ResponseEntity<>( returnFormFields, HttpStatus.OK );
        }
        catch ( Exception e )
        {
            return new ResponseEntity< ApiResponse >( new ApiResponse( "Cant find task with id [ " + form.getTask() + " ]", false ), HttpStatus.BAD_REQUEST );
        }

    }


    @GetMapping( "/start" )
    public ResponseEntity< ? > startTask( @RequestHeader( required = true, value = "Authorization" ) final String token )
    {

        List< TaskInfo > tasks = new ArrayList< TaskInfo >();
        Long extractId = this.jwtUtil.extractId( token );

        User user = this.iUserRepository.findById( extractId ).get();

        Role roleComplete = this.iRoleRepository.findByName( RoleName.ROLE_WRITER ).get();

        if ( user.getRoles().contains( roleComplete ) )
        {
            TaskInfo publishBook = new TaskInfo();
            TaskInfo appealPlagiarism = new TaskInfo();

            publishBook.setTitle( "Publish new book" );
            publishBook.setUrl( UrlStorage.HOST + UrlStorage.BOOK );

            tasks.add( publishBook );

            appealPlagiarism.setTask( "Appeal against plagiarism" );
            appealPlagiarism.setUrl( UrlStorage.HOST + UrlStorage.PLAGIARISM );

            tasks.add( appealPlagiarism );
        }

        return new ResponseEntity< List< TaskInfo > >( tasks, HttpStatus.OK );

    }


    @PostMapping( "/" )
    public ResponseEntity< ? > postTask( @RequestBody PostFormRequest form )
    {

        final Map< String, Object > map = new HashMap< String, Object >();

        map.putAll( form.getFields() );

        if ( form.getFields().containsKey( "vote" ) )
        {
            List< String > votes = ( List< String > ) this.runtimeService.getVariable( form.getProcess(), "votes" );

            String string = form.getFields().get( "vote" );
            votes.add( string );

            this.runtimeService.removeVariable( form.getProcess(), "votes" );
            this.runtimeService.setVariable( form.getProcess(), "votes", votes );

        }

        this.formService.submitTaskForm( form.getTask(), map );

        return new ResponseEntity< ApiResponse >( new ApiResponse( "Finished task", true ), HttpStatus.OK );

    }

}
