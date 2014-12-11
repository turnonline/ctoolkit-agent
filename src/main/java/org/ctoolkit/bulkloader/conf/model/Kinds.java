package org.ctoolkit.bulkloader.conf.model;

import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;

import java.util.List;

/**
 * @author <a href="mailto:medvegy@comvai.com">Aurel Medvegy</a>
 */
@Root
public class Kinds
{
    /**
     * List of kinds to export
     */
    @ElementList( required = false, inline = true )
    private List<Kind> kinds;

    /**
     * Default constructor
     */
    public Kinds()
    {
    }

    /**
     * @return the kinds
     */
    public List<Kind> getKinds()
    {
        return kinds;
    }

    /**
     * @param kinds the kinds to set
     */
    public void setKinds( List<Kind> kinds )
    {
        this.kinds = kinds;
    }
}
