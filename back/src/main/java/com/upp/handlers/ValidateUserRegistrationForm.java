package com.upp.handlers;

import java.util.HashMap;
import java.util.Map;


import com.upp.dtos.PostFormRequest;
import com.upp.repositories.IUserRepository;


import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ValidateUserRegistrationForm implements JavaDelegate
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

    @Override
    public void execute( DelegateExecution execution ) throws Exception
    {

        PostFormRequest form = ( PostFormRequest ) execution.getVariable( "form" );

        Map< String, String > fields = form.getFields();

        Map< String, String > errors = new HashMap< String, String >();

        Boolean valid = true;
        // // Task task = this.taskService.createTaskQuery().processInstanceId(
        // form.getProcess() ).singleResult();

        String email = "email";
        String stringEmail = fields.get( email );
        if ( this.iUserRepository.existsByEmail( stringEmail ) )
        {
            valid = false;
            errors.put( email, email + " must be unique" );
        }

        String username = "username";
        String stringUsername = fields.get( username );
        if ( this.iUserRepository.existsByUsername( stringUsername ) )
        {
            valid = false;
            errors.put( username, username + " must be unique" );
        }

        for ( Map.Entry< String, String > iterator : fields.entrySet() )
        {
            if ( iterator.getValue().isEmpty() )
            {
                valid = false;
                errors.put( iterator.getKey(), iterator.getKey() + " must be present" );

            }
        }

        execution.removeVariable( "errors" );
        execution.setVariable( "errors", errors );
        execution.setVariable( "dataValid", valid );

    }

}
