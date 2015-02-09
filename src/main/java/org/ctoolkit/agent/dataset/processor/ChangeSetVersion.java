package org.ctoolkit.agent.dataset.processor;

import java.util.Date;

/**
 * Change set operation history entry.
 *
 * @author <a href="mailto:aurel.medvegy@ctoolkit.org">Aurel Medvegy</a>
 */
public class ChangeSetVersion
        implements Comparable<ChangeSetVersion>
{
    private Long version;

    private Date created;

    private String user;

    /**
     * Default constructor
     */
    public ChangeSetVersion()
    {
        version = 0L;
        created = new Date();
        user = "unknown";
    }

    /**
     * Constructor.
     *
     * @param version the version number
     * @param user    the history entry author
     */
    public ChangeSetVersion( Long version, String user )
    {
        this.version = version;
        this.created = new Date();
        this.user = user;
    }

    /**
     * Returns the version number.
     *
     * @return the version number
     */
    public Long getVersion()
    {
        return version;
    }

    /**
     * Sets the version number.
     *
     * @param version the version number to be set
     */
    public void setVersion( Long version )
    {
        this.version = version;
    }

    /**
     * Returns the history entry creation date.
     *
     * @return the history entry creation date
     */
    public Date getCreated()
    {
        return created;
    }

    /**
     * Sets the history entry creation date.
     *
     * @param created the created date to be set
     */
    public void setCreated( Date created )
    {
        this.created = created;
    }

    /**
     * Returns the history entry author.
     *
     * @return the history entry author
     */
    public String getUser()
    {
        return user;
    }

    /**
     * Sets the history entry author.
     *
     * @param user the history entry author to be set
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
