package org.ctoolkit.bulkloader;

/**
 * Enum describing Job state
 *
 * @author <a href="mailto:medvegy@comvai.com">Aurel Medvegy</a>
 */
public enum JobState
{

    IDLE( 0 ),
    RUNNING( 1 );

    private final int state;

    JobState( int state )
    {
        this.state = state;
    }

    public int getState()
    {
        return state;
    }
}
