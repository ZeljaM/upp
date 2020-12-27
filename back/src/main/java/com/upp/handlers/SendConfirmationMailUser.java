package com.upp.handlers;

import com.upp.dtos.PostFormRequest;
import com.upp.models.User;
import com.upp.repositories.IUserRepository;
import com.upp.services.EmailService;


import org.apache.commons.lang3.RandomStringUtils;
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

        PostFormRequest form = ( PostFormRequest ) execution.getVariable( "form" );

        String email = form.getFields().get( "email" );

        String activationCode = RandomStringUtils.randomAlphanumeric( 10 );

        User user = this.iUserRepository.findByEmail( email ).get();
        user.setActivationCode( activationCode );

        // FIXME replace this please!
        String text = "http://localhost:8081/register/user/verify/" + execution.getProcessInstanceId() + "/" + currentTask.getId();

        emailService.sendMessage( email, "Please verify your account", text );

    }

}
