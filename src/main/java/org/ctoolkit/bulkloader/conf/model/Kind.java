package org.ctoolkit.bulkloader.conf.model;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Root;

/**
 * Configuration Kind
 *
 * @author <a href="mailto:medvegy@comvai.com">Aurel Medvegy</a>
 */
@Root
public class Kind
{

    @Attribute
    private String name;

    /**
     * Default constructor
     */
    public Kind()
    {
    }

    /**
     * Constructor
     *
     * @param name name of the kind
     */
    public Kind( String name )
    {
        this.name = name;
    }

    /**
     * @return the name
     */
    public String getName()
    {
        return name;
    }

    /**
     * @param name the name to set
     */
    public void setName( String name )
    {
        this.name = name;
    }

    @Override
    public String toString()
    {
        return "Kind{" +
                "name='" + name + '\'' +
                '}';
    }
}
