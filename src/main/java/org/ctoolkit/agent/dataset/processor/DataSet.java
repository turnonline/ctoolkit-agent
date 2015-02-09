package org.ctoolkit.agent.dataset.processor;

import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;

import java.util.List;

/**
 * The list of change set's key as a pointer to change sets.
 *
 * @author <a href="mailto:aurel.medvegy@ctoolkit.org">Aurel Medvegy</a>
 */
@Entity
public class DataSet
{
    @Id
    private Long id;

    /**
     * List of change set keys
     */
    private List<String> keys;

    private String pattern;

    private Source source;

    /**
     * Default constructor
     */
    public DataSet()
    {
    }

    public DataSet( Long id )
    {
        this.id = id;
    }

    public Long getId()
    {
        return id;
    }

    /**
     * Returns the file name pattern of data set that reside on local file system.
     *
     * @return the file name pattern
     */
    public String getPattern()
    {
        return pattern;
    }

    /**
     * Sets the file name pattern.
     *
     * @param pattern the file name pattern to be set
     */
    public void setPattern( String pattern )
    {
        this.pattern = pattern;
    }

    /**
     * Returns the enumeration value as a representation of the data set source.
     *
     * @return the source of data set
     */
    public Source getSource()
    {
        return source;
    }

    /**
     * Sets the source of data set.
     *
     * @param source the source of data set to be set
     */
    public void setSource( Source source )
    {
        this.source = source;
    }

    /**
     * Returns the list of change set's key as a pointer to change sets.
     *
     * @return the list of change set's key
     */
    public List<String> getKeys()
    {
        return keys;
    }

    /**
     * Sets the list of change set's key as a pointer to change sets.
     *
     * @param keys the keys to be set
     */
    public void setKeys( List<String> keys )
    {
        this.keys = keys;
    }

    /**
     * The enumeration as a representation of the data set source.
     */
    public static enum Source
    {
        LOCAL,
        DATASTORE
    }
}
