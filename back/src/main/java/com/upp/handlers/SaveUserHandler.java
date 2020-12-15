package com.upp.handlers;

import java.util.Collections;


import com.upp.dtos.PostFormRequest;
import com.upp.models.Role;
import com.upp.models.RoleName;
import com.upp.models.User;
import com.upp.repositories.IRoleRepository;
import com.upp.repositories.IUserRepository;


import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SaveUserHandler implements JavaDelegate
{

    // // @Autowired
    // // private IdentityService identityService;

    // // @Autowired
    // // private TaskService taskService;

    // // @Autowired
    // // private FormService formService;

    // // @Autowired
    // // private RuntimeService runtimeService;

    @Autowired
    private IUserRepository iUserRepository;

    @Autowired
    private IRoleRepository iRoleRepository;

    @Override
    public void execute( DelegateExecution execution ) throws Exception
    {
        PostFormRequest form = ( PostFormRequest ) execution.getVariable( "form" );

        final Role userRole = this.iRoleRepository.findByName( RoleName.ROLE_READER ).orElseThrow( () -> new Exception( "User Role not set." ) );

        User newUser = new User( form.getFields() );

        newUser.setRoles( Collections.singleton( userRole ) );

        // TODO: adding genres

        this.iUserRepository.save( newUser );

    }

}
