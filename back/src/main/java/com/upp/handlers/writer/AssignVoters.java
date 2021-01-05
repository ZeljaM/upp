package com.upp.handlers.writer;

import java.util.ArrayList;
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
public class AssignVoters implements JavaDelegate
{

    @Autowired
    private IUserRepository IUserRepository;

    @Autowired
    private EmailService EmailService;

    @Override
    public void execute( DelegateExecution execution ) throws Exception
    {
        System.err.println( execution );

        List< User > editors = this.IUserRepository.findByRolesName( RoleName.ROLE_EDITOR );

        for ( User u : editors )
        {
            this.EmailService.sendMessage( u.getEmail(), "Vote on registration", "Log in and vote!" );
        }

        ArrayList< String > list = new ArrayList<>( editors.stream().map( e -> e.getId().toString() ).collect( Collectors.toList() ) );
        execution.setVariable( "editors", list );

    }

}
