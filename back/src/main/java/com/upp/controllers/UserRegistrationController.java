package com.upp.controllers;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


import com.upp.configuration.UrlStorage;
import com.upp.dtos.ApiResponse;
import com.upp.dtos.FormFields;
import com.upp.dtos.PostFormRequest;
import com.upp.models.Genre;
import com.upp.repositories.IGenreRepository;


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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@RestController( )
@RequestMapping( value = "/register/user" )
public class UserRegistrationController
{

    @Autowired
    private TaskService taskService;

    @Autowired
    private FormService formService;

    @Autowired
    private RuntimeService runtimeService;

    @Autowired
    private IGenreRepository iGenreRepository;

    @GetMapping( "/process" )
    public ResponseEntity< FormFields > startProcess()
    {

        ProcessInstance instance = runtimeService.startProcessInstanceByKey( "register_user" );

        Task firstTask = this.taskService.createTaskQuery().processInstanceId( instance.getId() ).list().get( 0 );

        List< Genre > allGenres = this.iGenreRepository.findAll();
        String defaultValuesForGenres = allGenres.stream().map( g -> g.getName() ).collect( Collectors.joining( ";" ) );

        this.runtimeService.setVariable( instance.getId(), "defaultValues", defaultValuesForGenres );
        this.runtimeService.setVariable( instance.getId(), "completed", false );

        TaskFormData taskFormData = this.formService.getTaskFormData( firstTask.getId() );

        String formKey = taskFormData.getFormKey();

        List< FormField > formFields = taskFormData.getFormFields();

        FormFields returnFormFields = new FormFields( firstTask.getId(), instance.getId(), formFields, new HashMap< String, String >(), formKey,
                UrlStorage.HOST + UrlStorage.POST_READER );

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

        ResponseEntity< Object > postForEntity = restTemplate.postForEntity( "http://localhost:8081/register/user/task/", postFormRequest, Object.class );

        return postForEntity;

    }


    @SuppressWarnings( "unchecked" )
    @PostMapping( "/task" )
    public ResponseEntity< ? > postTask( @RequestBody PostFormRequest form )
    {

        final Map< String, Object > map = new HashMap< String, Object >();

        for ( Map.Entry< String, String > iterator : form.getFields().entrySet() )
        {
            map.put( iterator.getKey(), iterator.getValue() );
        }

        this.formService.submitTaskForm( form.getTask(), map );

        List< Task > tasks = this.taskService.createTaskQuery().processInstanceId( form.getProcess() ).list();
        if ( tasks.size() == 0 )
        {
            // ? if there is no tasks[0]
            ApiResponse apiResponse = new ApiResponse( "You can now sign in!", true );
            return new ResponseEntity< ApiResponse >( apiResponse, HttpStatus.CREATED );
        }
        else
        {
            Boolean finished = ( Boolean ) runtimeService.getVariable( form.getProcess(), "completed" );
            if ( finished )
            {
                ApiResponse apiResponse = new ApiResponse( "Verify account by email!", true );
                return new ResponseEntity< ApiResponse >( apiResponse, HttpStatus.CREATED );

            }

        }

        Task nextTask = tasks.get( 0 );

        TaskFormData taskFormData = this.formService.getTaskFormData( nextTask.getId() );
        String formKey = taskFormData.getFormKey();
        List< FormField > formFields = taskFormData.getFormFields();

        HashMap< String, String > errors = ( HashMap< String, String > ) runtimeService.getVariable( form.getProcess(), "errors" );
        FormFields returnFormFields =
                new FormFields( nextTask.getId(), form.getProcess(), formFields, errors, formKey, UrlStorage.HOST + UrlStorage.POST_READER );

        return new ResponseEntity< FormFields >( returnFormFields, HttpStatus.OK );

    }

}
