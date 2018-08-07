package org.ctoolkit.agent.model;

import java.util.Map;
import java.util.TreeMap;

/**
 * Value with labels adds prefixed labelled values to value.
 * For instance <pre>new ValueWithLabels(1).addLabel("name","__syncId").toString()</pre> will produce <pre>[name=__syncId]1</pre>.
 *
 * @author <a href="mailto:pohorelec@turnonlie.biz">Jozef Pohorelec</a>
 */
public class ValueWithLabels
{
    private Map<String, String> labels = new TreeMap<>();

    private Object value;

    public ValueWithLabels( Object value )
    {
        this.value = value;
    }

    public ValueWithLabels addLabel( String key, String value )
    {
        labels.put( key, value );
        return this;
    }

    @Override
    public String toString()
    {
        StringBuilder sb = new StringBuilder();
        for ( Map.Entry<String, String> entry : labels.entrySet() )
        {
            sb.append( "[" );
            sb.append( entry.getKey() );
            sb.append( ":" );
            sb.append( entry.getValue() );
            sb.append( "]" );
        }

        sb.append( value );

        return sb.toString();
    }
}
