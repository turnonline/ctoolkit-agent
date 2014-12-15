package org.ctoolkit.bulkloader.common;

import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;

/**
 * Enum for describing process progress state
 *
 * @author <a href="mailto:medvegy@comvai.com">Aurel Medvegy</a>
 */
public enum ProgressState
{

    IDLE( "IDLE" ),
    RUNNING( "RUNNING" ),
    DONE( "DONE" );

    private static final Map<String, ProgressState> lookup = new HashMap<String, ProgressState>();

    static
    {
        for ( ProgressState s : EnumSet.allOf( ProgressState.class ) )
        {
            lookup.put( s.getState(), s );
        }
    }

    private final String state;

    ProgressState( String state )
    {
        this.state = state;
    }

    public static ProgressState get( String state )
    {
        return lookup.get( state );
    }

    /**
     * @return the state
     */
    public String getState()
    {
        return state;
    }
}
