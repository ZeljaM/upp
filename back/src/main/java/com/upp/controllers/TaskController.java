package com.upp.controllers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


import com.upp.configuration.UrlStorage;
import com.upp.dtos.ApiResponse;
import com.upp.dtos.FormFields;
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
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
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


    @GetMapping( "/form" )
    public ResponseEntity< ? > form( @RequestParam final String process, @RequestParam String task )
    {
        try
        {

            TaskFormData taskFormData = this.formService.getTaskFormData( task );
            String formKey = taskFormData.getFormKey();
            List< FormField > formFields = taskFormData.getFormFields();

            FormFields returnFormFields = new FormFields( task, process, formFields, new HashMap<>(), formKey, UrlStorage.HOST );
            return new ResponseEntity<>( returnFormFields, HttpStatus.OK );
        }
        catch ( Exception e )
        {
            return new ResponseEntity< ApiResponse >( new ApiResponse( "Cant find task with id [ " + task + " ]", false ), HttpStatus.BAD_REQUEST );
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

}
