package com.upp.handlers;

import java.util.Optional;


import com.upp.models.Genre;
import com.upp.models.User;
import com.upp.repositories.IGenreRepository;
import com.upp.repositories.IUserRepository;


import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ProcessGenresBeta implements JavaDelegate
{

    @Autowired
    private IUserRepository iUserRepository;

    @Autowired
    private IGenreRepository iGenreRepository;

    @Override
    public void execute( DelegateExecution execution ) throws Exception
    {
        String email = ( String ) execution.getVariable( "email" );
        String genresBetaString = ( String ) execution.getVariable( "genresBeta" );
        execution.setVariable( "completed", true );

        Optional< User > findByEmail = this.iUserRepository.findByEmail( email );
        if ( findByEmail.isPresent() )
        {
            User user = findByEmail.get();
            String[] split = genresBetaString.split( ";" );
            for ( String s : split )
            {
                Optional< Genre > findByName = this.iGenreRepository.findByName( s );
                if ( findByName.isPresent() )
                {
                    user.getBetaGenres().add( findByName.get() );
                }
            }

            this.iUserRepository.save( user );
        }

    }

}
