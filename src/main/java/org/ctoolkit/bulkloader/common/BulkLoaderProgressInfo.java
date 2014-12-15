package org.ctoolkit.bulkloader.common;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * @author <a href="mailto:medvegy@comvai.com">Aurel Medvegy</a>
 */
public class BulkLoaderProgressInfo
        implements Serializable
{
    private static final long serialVersionUID = 1L;

    private Map<String, String> props = new HashMap<String, String>();

    /**
     * Default constructor
     */
    public BulkLoaderProgressInfo()
    {
    }

    /**
     * Copy constructor
     *
     * @param other
     */
    public BulkLoaderProgressInfo( final BulkLoaderProgressInfo other )
    {
        // copy the other prop list
        if ( null != other.getProps() )
        {
            for ( String key : other.getProps().keySet() )
            {
                this.props.put( key, other.getProps().get( key ) );
            }
        }
    }

    /**
     * Constructor
     *
     * @param maxVersion
     */
    public BulkLoaderProgressInfo( Long maxVersion )
    {
        setMaxVersion( maxVersion );
        setVersion( 0L );
        setSubVersion( 0L );
        setCursor( "" );
        setState( ProgressState.IDLE );

    }

    /**
     * Constructor
     *
     * @param maxVersion
     * @param cursor
     * @param state
     */
    public BulkLoaderProgressInfo( Long maxVersion, String cursor, String state )
    {
        setMaxVersion( maxVersion );
        setVersion( 0L );
        setSubVersion( 0L );
        setCursor( cursor );
        setState( state );
    }

    /**
     * Constructor
     *
     * @param version
     * @param maxVersion
     * @param cursor
     * @param state
     */
    public BulkLoaderProgressInfo( Long version, Long subVersion, Long maxVersion, String cursor, String state )
    {
        setMaxVersion( maxVersion );
        setVersion( version );
        setSubVersion( subVersion );
        setCursor( cursor );
        setState( state );
    }

    public static BulkLoaderProgressInfo buildProgressInfo( Map<String, String[]> parameterMap )
    {
        BulkLoaderProgressInfo progressInfo = new BulkLoaderProgressInfo();
        for ( String key : parameterMap.keySet() )
        {
            progressInfo.setProp( key, getParameter( key, parameterMap ) );
        }

        return progressInfo;
    }

    /**
     * Return parameter value by specified parameter name
     *
     * @param parameterName parameter name to lookup
     * @param parameterMap  list of parameters
     * @return parameter value by specified parameter name
     */
    private static String getParameter( String parameterName, Map<String, String[]> parameterMap )
    {
        String[] param = parameterMap.get( parameterName );
        if ( param != null )
        {
            String value = param[0];
            if ( !isEmpty( value ) )
            {
                return value;
            }
        }

        return null;
    }

    private static boolean isEmpty( String string )
    {
        return string == null || string.trim().isEmpty();
    }

    public Map<String, String> getProps()
    {
        return props;
    }

    public void setProps( Map<String, String> props )
    {
        this.props = props;
    }

    public void setProp( String key, String value )
    {
        this.props.put( key, value );
    }

    public String getProp( String key )
    {
        return this.props.get( key );
    }

    /**
     * @return the state
     */
    public ProgressState getState()
    {
        return ProgressState.get( getProp( BulkLoaderConstants.PROP_STATE ) );
    }

    /**
     * @param state the state to set
     */
    public void setState( ProgressState state )
    {
        setProp( BulkLoaderConstants.PROP_STATE, state.name() );
    }

    /**
     * @param state the state to set
     */
    public void setState( String state )
    {
        setProp( BulkLoaderConstants.PROP_STATE, state );
    }

    /**
     * @return the version
     */
    public Long getVersion()
    {
        return getLongValue( BulkLoaderConstants.PROP_VERSION );
    }

    /**
     * @param version the version to set
     */
    public void setVersion( Long version )
    {
        setLongValue( BulkLoaderConstants.PROP_VERSION, version );
    }

    /**
     * @return the cursor
     */
    public String getCursor()
    {
        return getProp( BulkLoaderConstants.PROP_CURSOR );
    }

    /**
     * @param cursor the cursor to set
     */
    public void setCursor( String cursor )
    {
        setProp( BulkLoaderConstants.PROP_CURSOR, cursor );
    }

    /**
     * @return the toVersion
     */
    public Long getMaxVersion()
    {
        return getLongValue( BulkLoaderConstants.PROP_VERSION );
    }

    /**
     * @param maxVersion the toVersion to set
     */
    public void setMaxVersion( Long maxVersion )
    {
        setLongValue( BulkLoaderConstants.PROP_MAXVERSION, maxVersion );
    }

    /**
     * @return the subVersion
     */
    public Long getSubVersion()
    {
        return getLongValue( BulkLoaderConstants.PROP_SUBVERSION );
    }

    /**
     * @param subVersion the subVersion to set
     */
    public void setSubVersion( Long subVersion )
    {
        setLongValue( BulkLoaderConstants.PROP_SUBVERSION, subVersion );
    }

    public Long getLongValue( String key )
    {
        String ver = getProp( key );
        return null == ver ? null : Long.valueOf( ver );
    }

    public void setLongValue( String key, Long value )
    {
        if ( null == value )
        {
            setProp( key, null );
        }
        else
        {
            setProp( key, value.toString() );
        }
    }

    /* (non-Javadoc)
      * @see java.lang.Object#toString()
      */
    @Override
    public String toString()
    {
        StringBuffer sb = new StringBuffer( "BulkLoaderProgressInfo [" );
        int size = props.size();
        for ( String key : props.keySet() )
        {
            sb.append( key ).append( "=" ).append( props.get( key ) );
            if ( --size > 0 )
            {
                sb.append( ", " );
            }
        }
        return sb.append( "]" ).toString();
    }
}
