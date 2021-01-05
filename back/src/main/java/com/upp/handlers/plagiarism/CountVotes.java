package com.upp.handlers.plagiarism;

import java.util.ArrayList;


import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.stereotype.Service;

@Service
public class CountVotes implements JavaDelegate
{

    @Override
    public void execute( DelegateExecution execution ) throws Exception
    {
        ArrayList< String > votes = ( ArrayList< String > ) execution.getVariable( "votes" );
        int size = votes.size();

        if ( votes.contains( "false" ) && votes.contains( "true" ) )
        {
            // ! not unanimously
            execution.setVariable( "unanimously", false );
            execution.setVariable( "votes", new ArrayList< String >() );
        }
        else if ( votes.contains( "true" ) )
        {
            execution.setVariable( "unanimously", true );
            execution.setVariable( "isPlagiarism", true );
        }
        else
        {
            execution.setVariable( "unanimously", true );
            execution.setVariable( "isPlagiarism", false );

        }

    }

}
