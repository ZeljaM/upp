package com.upp.controllers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


import com.upp.configuration.UrlStorage;
import com.upp.dtos.FormFields;
import com.upp.models.RoleName;
import com.upp.models.User;
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
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping( "/api/plagiarism" )
public class PlagiarismController
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


    @GetMapping( "/process" )
    public ResponseEntity< FormFields > startProcess( @RequestHeader( required = true, value = "Authorization" ) final String token )
    {
        ProcessInstance instance = runtimeService.startProcessInstanceByKey( "plagiarism" );
        Task firstTask = this.taskService.createTaskQuery().processInstanceId( instance.getId() ).list().get( 0 );

        Long id = this.jwtUtil.extractId( token );

        List< User > findByRolesName = this.iUserRepository.findByRolesName( RoleName.ROLE_MODERATOR );

        User moderator = findByRolesName.get( 0 );

        this.runtimeService.setVariable( instance.getId(), "comments", new ArrayList< String >() );
        this.runtimeService.setVariable( instance.getId(), "votes", new ArrayList< String >() );
        this.runtimeService.setVariable( instance.getId(), "moderator", moderator.getId().toString() );
        this.runtimeService.setVariable( instance.getId(), "userId", id.toString() );

        TaskFormData taskFormData = this.formService.getTaskFormData( firstTask.getId() );
        String formKey = taskFormData.getFormKey();

        List< FormField > formFields = taskFormData.getFormFields();

        FormFields returnFormFields =
                new FormFields( firstTask.getId(), instance.getId(), formFields, new HashMap< String, String >(), formKey, UrlStorage.HOST + UrlStorage.TASK );

        return new ResponseEntity< FormFields >( returnFormFields, HttpStatus.OK );

    }

}
