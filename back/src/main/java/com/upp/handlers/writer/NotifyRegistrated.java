package com.upp.handlers.writer;

import java.util.HashSet;


import com.upp.models.Role;
import com.upp.models.RoleName;
import com.upp.models.User;
import com.upp.repositories.IRoleRepository;
import com.upp.repositories.IUserRepository;
import com.upp.services.EmailService;


import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class NotifyRegistrated implements JavaDelegate
{

    @Autowired
    private IUserRepository iUserRepository;

    @Autowired
    private IRoleRepository iRoleRepository;

    @Autowired
    private EmailService EmailService;

    @Override
    public void execute( DelegateExecution execution ) throws Exception
    {
        String email = ( String ) execution.getVariable( "email" );

        User user = this.iUserRepository.findByEmail( email ).get();

        user.getRoles().clear();

        user.setRoles( new HashSet< Role >() );

        Role role = this.iRoleRepository.findByName( RoleName.ROLE_WRITER ).get();

        user.getRoles().add( role );

        this.iUserRepository.save( user );

        this.EmailService.sendMessage( user.getEmail(), "Payment successful", "You have completed your registration!!!" );

    }

}
