package com.upp.controllers;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


import com.upp.dtos.FormFields;
import com.upp.dtos.PostFormRequest;


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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController( )
@RequestMapping( value = "/register/user" )
public class UserRegistrationController
{

    // // @Autowired
    // // private IdentityService identityService;

    @Autowired
    private TaskService taskService;

    @Autowired
    private FormService formService;

    @Autowired
    private RuntimeService runtimeService;

    @GetMapping( "/process" )
    public ResponseEntity< FormFields > startProcess()
    {

        ProcessInstance instance = runtimeService.startProcessInstanceByKey( "register_user" );

        Task firstTask = this.taskService.createTaskQuery().processInstanceId( instance.getId() ).list().get( 0 );

        TaskFormData taskFormData = this.formService.getTaskFormData( firstTask.getId() );

        List< FormField > formFields = taskFormData.getFormFields();

        FormFields returnFormFields = new FormFields( firstTask.getId(), instance.getId(), formFields );

        return new ResponseEntity< FormFields >( returnFormFields, HttpStatus.OK );

    }


    @SuppressWarnings( "unchecked" )
    @PostMapping( "/task" )
    public ResponseEntity< FormFields > postTask( @RequestBody PostFormRequest form )
    {

        // // Task currentTask = this.taskService.createTaskQuery().taskId(
        // // form.getTask() ).singleResult();

        final Map< String, Object > map = new HashMap< String, Object >();

        // ! retrieve previous form data
        PostFormRequest posts = ( PostFormRequest ) runtimeService.getVariable( form.getProcess(), "form" );

        if ( posts != null )
        {
            map.putAll( posts.getFields() );
        }
        else
        {
            posts = form;
        }

        // ! merge new and old form data
        for ( Map.Entry< String, String > iterator : form.getFields().entrySet() )
        {
            // ! the same key -> override value
            map.put( iterator.getKey(), iterator.getValue() );
            posts.getFields().put( iterator.getKey(), iterator.getValue() );
        }
        this.runtimeService.setVariable( form.getProcess(), "form", posts );

        this.formService.submitTaskForm( form.getTask(), map );

        List< Task > tasks = this.taskService.createTaskQuery().processInstanceId( form.getProcess() ).list();

        if ( tasks.size() == 0 )
        {
            // if there is no tasks[0]
            return new ResponseEntity<>( HttpStatus.CREATED );
        }

        Task nextTask = tasks.get( 0 );

        TaskFormData taskFormData = this.formService.getTaskFormData( nextTask.getId() );
        List< FormField > formFields = taskFormData.getFormFields();

        HashMap< String, String > errors = ( HashMap< String, String > ) runtimeService.getVariable( form.getProcess(), "errors" );
        FormFields returnFormFields = new FormFields( nextTask.getId(), form.getProcess(), formFields, errors );

        return new ResponseEntity< FormFields >( returnFormFields, HttpStatus.OK );

    }

}
