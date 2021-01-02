package com.upp.handlers.writer;

import java.util.ArrayList;
import java.util.List;


import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.stereotype.Service;

@Service
public class ProcessVoting implements JavaDelegate
{

    Integer positive = 0;

    Integer negative = 0;

    Boolean again = false;

    @Override
    public void execute( DelegateExecution execution ) throws Exception
    {

        positive = 0;
        negative = 0;
        again = false;

        List< String > votes = ( List< String > ) execution.getVariable( "votes" );

        Integer notifyCount = ( Integer ) execution.getVariable( "notifyCount" );
        Integer voteCount = ( Integer ) execution.getVariable( "voteCount" );

        int size = votes.size();

        votes.forEach( v ->
        {
            if ( v.equals( "more" ) )
            {

                again = true;

            }
            else if ( v.equals( "yes" ) )
            {
                ++positive;
            }
            else if ( v.equals( "no" ) )
            {
                ++negative;
            }
        } );

        execution.setVariable( "votes", new ArrayList< String >() );

        if ( again )
        {
            execution.setVariable( "moreFiles", true );
            execution.removeVariable( "notifyCount" );
            execution.setVariable( "notifyCount", notifyCount + 1 );

            execution.removeVariable( "voteCount" );
            execution.setVariable( "voteCount", ++voteCount );
        }
        else if ( positive == size )
        {
            execution.removeVariable( "voteAgain" );
            execution.setVariable( "voteAgain", false );

            execution.removeVariable( "rejected" );
            execution.setVariable( "rejected", false );

            execution.removeVariable( "moreFiles" );
            execution.setVariable( "moreFiles", false );
        }
        else if ( negative == size )
        {
            execution.removeVariable( "voteAgain" );
            execution.setVariable( "voteAgain", false );

            execution.removeVariable( "rejected" );
            execution.setVariable( "rejected", true );

            execution.removeVariable( "moreFiles" );
            execution.setVariable( "moreFiles", false );
        }
        else
        {
            execution.removeVariable( "voteAgain" );
            execution.setVariable( "voteAgain", true );

            execution.removeVariable( "moreFiles" );
            execution.setVariable( "moreFiles", false );

            execution.removeVariable( "voteCount" );
            execution.setVariable( "voteCount", ++voteCount );

        }

    }

}
