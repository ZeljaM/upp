package com.upp.handlers;

import java.util.Collections;


import com.upp.dtos.PostFormRequest;
import com.upp.models.Genre;
import com.upp.models.Role;
import com.upp.models.RoleName;
import com.upp.models.User;
import com.upp.repositories.IGenreRepository;
import com.upp.repositories.IRoleRepository;
import com.upp.repositories.IUserRepository;


import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SaveUserHandler implements JavaDelegate
{

    @Autowired
    private IUserRepository iUserRepository;

    @Autowired
    private IRoleRepository iRoleRepository;

    @Autowired
    private IGenreRepository iGenreRepository;

    @Override
    public void execute( DelegateExecution execution ) throws Exception
    {
        PostFormRequest form = ( PostFormRequest ) execution.getVariable( "form" );

        final Role userRole = this.iRoleRepository.findByName( RoleName.ROLE_READER ).orElseThrow( () -> new Exception( "User Role not set." ) );

        User newUser = new User( form.getFields() );

        newUser.setRoles( Collections.singleton( userRole ) );

        String genresString = form.getFields().get( "genres" );

        String genresBetaString = form.getFields().get( "genresBeta" );

        String[] genres = genresString.split( ";" );
        String[] genresBeta;
        if ( genresBetaString != null )
        {
            genresBeta = genresBetaString.split( ";" );

            for ( String name : genresBeta )
            {
                Genre genre = this.iGenreRepository.findByName( name ).get();

                newUser.getBetaGenres().add( genre );

            }
        }

        for ( String name : genres )
        {
            Genre genre = this.iGenreRepository.findByName( name ).get();

            newUser.getGenres().add( genre );

        }

        this.iUserRepository.save( newUser );

    }

}
