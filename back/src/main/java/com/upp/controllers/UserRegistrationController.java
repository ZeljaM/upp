package com.upp.controllers;

import org.camunda.bpm.engine.FormService;
import org.camunda.bpm.engine.IdentityService;
import org.camunda.bpm.engine.RuntimeService;
import org.camunda.bpm.engine.TaskService;
import org.camunda.bpm.engine.runtime.ProcessInstance;
import org.camunda.bpm.engine.task.Task;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController( )
@RequestMapping( value = "/dummy" )
public class UserRegistrationController
{

    @Autowired
    private IdentityService identityService;

    @Autowired
    private TaskService taskService;

    @Autowired
    private FormService formService;

    @Autowired
    private RuntimeService runtimeService;

    @GetMapping( "/start" )
    public String start()
    {

        ProcessInstance instance = runtimeService.startProcessInstanceByKey( "register_user" );

        Task task = taskService.createTaskQuery().processInstanceId( instance.getId() ).list().get( 0 );

        task.setAssignee( "demo" );

        taskService.saveTask( task );

        return "";

    }

}
