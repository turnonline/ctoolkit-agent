package org.ctoolkit.agent.dataset.processor;

import java.io.Serializable;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;

/**
 * The progress information holder of data set processing.
 *
 * @author <a href="mailto:aurel.medvegy@ctoolkit.org">Aurel Medvegy</a>
 */
public class ProgressInfo
        implements Serializable
{
    private static final long serialVersionUID = 1L;

    private final static String PROP_VERSION = "__agent_prop_version";

    private final static String PROP_SUBVERSION = "__agent_prop_subversion";

    private final static String PROP_MAX_VERSION = "__agent_prop_maxversion";

    private final static String PROP_CURSOR = "__agent_prop_cursor";

    private final static String PROP_STATE = "__agent_prop_state";

    private Map<String, String> properties = new HashMap<>();

    /**
     * Default constructor.
     */
    public ProgressInfo()
    {
    }

    /**
     * The constructor as a copy of given instance.
     *
     * @param other the instance to be copied
     */
    public ProgressInfo( ProgressInfo other )
    {
        // copy the other prop list
        if ( null != other.getProperties() )
        {
            for ( String key : other.getProperties().keySet() )
            {
                this.properties.put( key, other.getProperties().get( key ) );
            }
        }
    }

    /**
     * Constructor.
     *
     * @param maxVersion the max version of data set to process
     */
    public ProgressInfo( Long maxVersion )
    {
        setMaxVersion( maxVersion );
        setVersion( 0L );
        setSubVersion( 0L );
        setCursor( "" );
        setState( State.IDLE );

    }

    /**
     * Constructor.
     *
     * @param maxVersion the max version of data set to process
     * @param cursor     the marker of the current point of processing
     * @param state      the overall progress state
     */
    public ProgressInfo( Long maxVersion, String cursor, String state )
    {
        setMaxVersion( maxVersion );
        setVersion( 0L );
        setSubVersion( 0L );
        setCursor( cursor );
        setState( state );
    }

    /**
     * Constructor.
     *
     * @param version    the version of data set to start processing
     * @param subVersion the subversion of data set to start processing
     * @param maxVersion the max version of data set to process
     * @param cursor     the marker of the current point of processing
     * @param state      the overall progress state
     */
    public ProgressInfo( Long version, Long subVersion, Long maxVersion, String cursor, String state )
    {
        setMaxVersion( maxVersion );
        setVersion( version );
        setSubVersion( subVersion );
        setCursor( cursor );
        setState( state );
    }

    /**
     * Returns the properties of this instance as a map.
     *
     * @return the properties map
     */
    public Map<String, String> getProperties()
    {
        return properties;
    }

    /**
     * Returns the value to which the specified property key is mapped, or {@code null}
     * if there is no mapping for the given key.
     *
     * @param key the property key whose associated value is to be returned
     * @return the value to which the specified property key is mapped
     */
    public String getProperty( String key )
    {
        return this.properties.get( key );
    }

    /**
     * Sets the property.
     *
     * It associates the specified property value with the specified property key.
     * If this previously contained a mapping for the property key, the old value is replaced by the specified value.
     *
     * @param key   the property key with which the specified value is to be associated
     * @param value the property value to be associated with the specified key
     */
    public void setProperty( String key, String value )
    {
        this.properties.put( key, value );
    }

    /**
     * Returns the boolean indication whether current data set job has successfully completed or not.
     *
     * @return true if current data set job has successfully completed or not
     */
    public boolean isCompleted()
    {
        return getState() == State.COMPLETED;
    }

    /**
     * Returns the overall progress state.
     *
     * @return the overall progress state
     */
    public State getState()
    {
        return State.get( getProperty( PROP_STATE ) );
    }

    /**
     * Sets the overall progress state.
     *
     * @param state the state to be set as string
     */
    public void setState( String state )
    {
        setProperty( PROP_STATE, state );
    }

    /**
     * Sets the overall progress state.
     *
     * @param state the state to be set as enum
     */
    public void setState( State state )
    {
        setProperty( PROP_STATE, state.name() );
    }

    /**
     * Returns the version of data set to start processing.
     *
     * @return the version of data set to start processing
     */
    public Long getVersion()
    {
        return getLongValue( PROP_VERSION );
    }

    /**
     * Sets the version of data set to start processing.
     *
     * @param version the version to be set
     */
    public void setVersion( Long version )
    {
        setLongValue( PROP_VERSION, version );
    }

    /**
     * Returns the marker of the current point of processing.
     *
     * @return the current cursor
     */
    public String getCursor()
    {
        return getProperty( PROP_CURSOR );
    }

    /**
     * Sets the marker of the current point of processing.
     *
     * @param cursor the cursor to be set
     */
    public void setCursor( String cursor )
    {
        setProperty( PROP_CURSOR, cursor );
    }

    /**
     * Returns the max version of data set to process.
     *
     * @return the max version
     */
    public Long getMaxVersion()
    {
        return getLongValue( PROP_VERSION );
    }

    /**
     * Sets the max version of data set to process.
     *
     * @param maxVersion the max version to be set
     */
    public void setMaxVersion( Long maxVersion )
    {
        setLongValue( PROP_MAX_VERSION, maxVersion );
    }

    /**
     * Returns the sub version.
     *
     * @return the sub version
     */
    public Long getSubVersion()
    {
        return getLongValue( PROP_SUBVERSION );
    }

    /**
     * Sets the sub version.
     *
     * @param subVersion the sub version to be set
     */
    public void setSubVersion( Long subVersion )
    {
        setLongValue( PROP_SUBVERSION, subVersion );
    }

    private Long getLongValue( String key )
    {
        String ver = getProperty( key );
        return null == ver ? null : Long.valueOf( ver );
    }

    private void setLongValue( String key, Long value )
    {
        if ( null == value )
        {
            setProperty( key, null );
        }
        else
        {
            setProperty( key, value.toString() );
        }
    }

    @Override
    public String toString()
    {
        return "ProgressInfo{" +
                "properties=" + properties +
                '}';
    }

    /**
     * Enum to describe overall process progress state.
     *
     * @author <a href="mailto:aurel.medvegy@ctoolkit.org">Aurel Medvegy</a>
     */
    public static enum State
    {

        IDLE( "IDLE" ),
        RUNNING( "RUNNING" ),
        COMPLETED( "COMPLETED" );

        private static final Map<String, State> lookup = new HashMap<>();

        static
        {
            for ( State s : EnumSet.allOf( State.class ) )
            {
                lookup.put( s.state, s );
            }
        }

        private final String state;

        State( String state )
        {
            this.state = state;
        }

        public static State get( String state )
        {
            return lookup.get( state );
        }
    }
}
