package com.upp.handlers;

import com.upp.configuration.UrlStorage;
import com.upp.repositories.IUserRepository;
import com.upp.services.EmailService;


import org.camunda.bpm.engine.TaskService;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.camunda.bpm.engine.task.Task;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SendConfirmationMailUser implements JavaDelegate
{

    @Autowired
    private EmailService emailService;

    @Autowired
    private IUserRepository iUserRepository;

    @Autowired
    private TaskService taskService;

    @Override
    public void execute( DelegateExecution execution ) throws Exception
    {

        Task currentTask = this.taskService.createTaskQuery().processInstanceId( execution.getProcessInstanceId() ).singleResult();

        String email = ( String ) execution.getVariable( "email" );

        this.iUserRepository.findByEmail( email ).get();
        String text = "<a href='" + UrlStorage.HOST + UrlStorage.VERIFY_READER + execution.getProcessInstanceId() + "/" + currentTask.getId() + "'>Verify</a>";

        emailService.sendMessage( email, "Please verify your account", text );

    }

}
