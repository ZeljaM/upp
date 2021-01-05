package com.upp.handlers.plagiarism;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


import com.upp.models.RoleName;
import com.upp.models.User;
import com.upp.repositories.IUserRepository;


import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SendNotificationToEditors implements JavaDelegate
{

    @Autowired
    private IUserRepository iUserRepository;

    @Override
    public void execute( DelegateExecution execution ) throws Exception
    {
        String editors = ( String ) execution.getVariable( "selectedEditors" );

        String[] split = editors.split( ";" );

        List< User > list = new ArrayList<>();

        for ( String s : split )
        {
            Optional< User > findByEmail = this.iUserRepository.findByEmail( s );

            if ( findByEmail.isPresent() )
            {
                list.add( findByEmail.get() );
            }

        }

        List< String > collect1 = list.stream().map( e -> e.getId().toString() ).collect( Collectors.toList() );

        execution.setVariable( "usersToComment", collect1 );

        List< User > findByRolesName = this.iUserRepository.findByRolesName( RoleName.ROLE_EDITOR );

        List< String > collect2 = findByRolesName.stream().map( frn -> frn.getId().toString() ).collect( Collectors.toList() );

        execution.setVariable( "usersToVote", collect2 );

    }

}
