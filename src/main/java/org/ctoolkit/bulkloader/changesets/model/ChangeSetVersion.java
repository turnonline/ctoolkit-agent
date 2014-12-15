package org.ctoolkit.bulkloader.changesets.model;

import java.util.Date;

/**
 * ChangeSetHistory entry
 *
 * @author <a href="mailto:medvegy@comvai.com">Aurel Medvegy</a>
 */
public class ChangeSetVersion
        implements Comparable<ChangeSetVersion>
{
    /**
     * Version number
     */
    private Long version;

    /**
     * History entry creation date
     */
    private Date created;

    /**
     * History entry author
     */
    private String user;

    /**
     * Default constructor
     */
    public ChangeSetVersion()
    {
        version = ( long ) 0;
        created = new Date();
        user = "unknown";
    }

    /**
     * @param version
     * @param user
     */
    public ChangeSetVersion( Long version, String user )
    {
        this.version = version;
        this.created = new Date();
        this.user = user;
    }

    /**
     * @return the version
     */
    public Long getVersion()
    {
        return version;
    }

    /**
     * @param version the version to set
     */
    public void setVersion( Long version )
    {
        this.version = version;
    }

    /**
     * @return the created
     */
    public Date getCreated()
    {
        return created;
    }

    /**
     * @param created the created to set
     */
    public void setCreated( Date created )
    {
        this.created = created;
    }

    /**
     * @return the user
     */
    public String getUser()
    {
        return user;
    }

    /**
     * @param user the user to set
     */
    public void setUser( String user )
    {
        this.user = user;
    }

    @Override
    public int compareTo( ChangeSetVersion o )
    {
        return version.compareTo( o.getVersion() );
    }

    @Override
    public String toString()
    {
        return "Version " + version + ", created by " + user + " on " + created;
    }
}
