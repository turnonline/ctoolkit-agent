package org.ctoolkit.bulkloader.conf.model;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

/**
 * @author <a href="mailto:medvegy@comvai.com">Aurel Medvegy</a>
 */
@Root( name = "export-job" )
public class ExportJob
{

    /**
     * Name of the export configuration. There can be more exports described
     * within a single configuration file distinguished by their names.
     */
    @Attribute
    private String name;

    @Attribute( required = false )
    private Boolean fullExport;

    @Element( required = false )
    private Kinds kinds;

    /**
     * Explicit constructor
     */
    public ExportJob()
    {
    }

    /**
     * Constructor to initialize the export configuration bean
     *
     * @param name name of the export task
     */
    public ExportJob( String name )
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

    /**
     * @return the kinds
     */
    public Kinds getKinds()
    {
        return kinds;
    }

    /**
     * @param kinds the kinds to set
     */
    public void setKinds( Kinds kinds )
    {
        this.kinds = kinds;
    }

    /**
     * @return the fullExport
     */
    public Boolean getFullExport()
    {
        return fullExport;
    }

    /**
     * @param fullExport the fullExport to set
     */
    public void setFullExport( Boolean fullExport )
    {
        this.fullExport = fullExport;
    }

    public boolean hasKinds()
    {
        return ( null != kinds.getKinds() && 0 < kinds.getKinds().size() );
    }

    @Override
    public String toString()
    {
        return "ExportJob{" +
                "name='" + name + '\'' +
                ", fullExport=" + fullExport +
                ", kinds=" + kinds +
                '}';
    }
}
