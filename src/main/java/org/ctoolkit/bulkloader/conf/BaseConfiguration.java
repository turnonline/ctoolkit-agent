package org.ctoolkit.bulkloader.conf;

import java.util.Properties;

/**
 * @author <a href="mailto:medvegy@comvai.com">Aurel Medvegy</a>
 */
public abstract class BaseConfiguration
        implements Configuration
{

    private final Properties props = new Properties();

    @Override
    public void setProperty( String key, Object value )
    {
        props.put( key, value );
    }

    @Override
    public Object getProperty( String key )
    {
        return props.get( key );
    }

    public void addProperties( Properties props )
    {
        props.putAll( props );
    }
}
