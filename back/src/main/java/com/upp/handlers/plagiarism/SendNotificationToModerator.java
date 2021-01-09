package com.upp.handlers.plagiarism;

import java.util.List;
import java.util.stream.Collectors;


import com.upp.models.RoleName;
import com.upp.models.User;
import com.upp.repositories.IUserRepository;
import com.upp.services.EmailService;


import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SendNotificationToModerator implements JavaDelegate
{

    @Autowired
    private IUserRepository iUserRepository;

    @Autowired
    private EmailService EmailService;

    @Override
    public void execute( DelegateExecution execution ) throws Exception
    {

        String moderatorId = ( String ) execution.getVariable( "moderator" );

        User moderator = this.iUserRepository.findById( Long.parseLong( moderatorId ) ).get();

        this.EmailService.sendMessage( moderator.getEmail(), "Started plagiarism appeal", "Plagiarism appeal has started! Pick editors to review" );

        List< User > editors = this.iUserRepository.findByRolesName( RoleName.ROLE_EDITOR );

        String collect = editors.stream().map( e -> e.getUsername().toString() ).collect( Collectors.joining( ";" ) );

        execution.setVariable( "allEditors", collect );

    }

}
