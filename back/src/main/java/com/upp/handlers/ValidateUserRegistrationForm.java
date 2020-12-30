package com.upp.handlers;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;


import com.upp.models.Genre;
import com.upp.models.Role;
import com.upp.models.RoleName;
import com.upp.models.User;
import com.upp.repositories.IGenreRepository;
import com.upp.repositories.IRoleRepository;
import com.upp.repositories.IUserRepository;


import org.camunda.bpm.engine.FormService;
import org.camunda.bpm.engine.TaskService;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.camunda.bpm.engine.form.FormField;
import org.camunda.bpm.engine.form.TaskFormData;
import org.camunda.bpm.engine.task.Task;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class ValidateUserRegistrationForm implements JavaDelegate
{

    @Autowired
    private TaskService taskService;

    @Autowired
    private FormService formService;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    private IUserRepository iUserRepository;

    @Autowired
    private IGenreRepository iGenreRepository;

    @Autowired
    private IRoleRepository iRoleRepository;

    private static final String REQUIRED = "required";

    private static final String MINLENGTH = "minlength";

    private static final String MAXLENGTH = "maxlength";

    private static final String MIN = "min";

    private static final String MAX = "max";

    private static final String USERNAME = "username";

    private static final String EMAIL = "email";

    @Override
    public void execute( DelegateExecution execution ) throws Exception
    {

        Map< String, String > errors = new HashMap< String, String >();

        Task currentTask = this.taskService.createTaskQuery().processInstanceId( execution.getProcessInstanceId() ).singleResult();
        TaskFormData taskFormData = this.formService.getTaskFormData( currentTask.getId() );

        taskFormData.getFormFields().forEach( ff ->
        {
            try
            {

                Object value = ff.getValue().getValue();

                Map< String, String > properties = ff.getProperties();

                properties.forEach( ( k, v ) ->
                {
                    if ( k.equals( ValidateUserRegistrationForm.USERNAME ) )
                    {
                        Boolean existsByUsername = this.iUserRepository.existsByUsername( ff.getValue().getValue().toString() );
                        if ( existsByUsername )
                        {
                            errors.put( ff.getId(), "Already exists " + ff.getId() );
                        }
                    }
                    else if ( k.equals( ValidateUserRegistrationForm.EMAIL ) )
                    {
                        Boolean email = this.iUserRepository.existsByEmail( ff.getValue().getValue().toString() );
                        if ( email )
                        {
                            errors.put( ff.getId(), "Already exists " + ff.getId() );
                        }
                    }
                    else if ( k.equals( ValidateUserRegistrationForm.REQUIRED ) && value.equals( null ) )
                    {
                        errors.put( ff.getId(), ff.getId() + " is required" );
                    }
                    else if ( k.equals( ValidateUserRegistrationForm.MINLENGTH )
                            && ( ( String ) ff.getValue().getValue() ).length() < Integer.parseInt( v.toString() ) )
                    {
                        errors.put( ff.getId(), ff.getId() + " minimum length is " + v.toString() );

                    }
                    else if ( k.equals( ValidateUserRegistrationForm.MAXLENGTH )
                            && ( ( String ) ff.getValue().getValue() ).length() > Integer.parseInt( v.toString() ) )
                    {
                        errors.put( ff.getId(), ff.getId() + " maximum length is " + v.toString() );

                    }

                    else if ( k.equals( ValidateUserRegistrationForm.MIN ) && ( Long ) ff.getValue().getValue() < Long.parseLong( v.toString() ) )
                    {
                        errors.put( ff.getId(), ff.getId() + " minimum value is " + v.toString() );

                    }
                    else if ( k.equals( ValidateUserRegistrationForm.MAX ) && ( Long ) ff.getValue().getValue() > Long.parseLong( v.toString() ) )
                    {
                        errors.put( ff.getId(), ff.getId() + " minimum value is " + v.toString() );

                    }

                } );

            }
            catch ( Exception e )
            {
                errors.put( ff.getId(), "Cant find value!" );
            }

        } );

        execution.setVariable( "dataValid", errors.isEmpty() );

        execution.removeVariable( "errors" );
        execution.setVariable( "errors", errors );

        if ( errors.isEmpty() )
        {
            List< FormField > formFields = taskFormData.getFormFields();

            Map< String, String > collect = formFields.stream().collect( Collectors.toMap( FormField::getId, i -> i.getValue().getValue().toString() ) );

            User newUser = new User( collect );
            newUser.setPassword( this.passwordEncoder.encode( newUser.getPassword() ) );

            Role readerRole = this.iRoleRepository.findByName( RoleName.ROLE_READER ).get();

            newUser.getRoles().add( readerRole );

            if ( newUser.getBeta() )
            {
                Role beta = this.iRoleRepository.findByName( RoleName.ROLE_BETA ).get();
                newUser.getRoles().add( beta );

            }

            String genres = ( String ) execution.getVariable( "genres" );
            String[] split = genres.split( ";" );

            for ( String s : split )
            {
                Optional< Genre > findByName = this.iGenreRepository.findByName( s );
                if ( findByName.isPresent() )
                {
                    newUser.getGenres().add( findByName.get() );
                }
            }

            this.iUserRepository.save( newUser );

            if ( newUser.getBeta() )
            {
                execution.setVariable( "beta", newUser.getBeta() );
            }
            else
            {
                execution.setVariable( "completed", true );

            }

        }

    }

}
