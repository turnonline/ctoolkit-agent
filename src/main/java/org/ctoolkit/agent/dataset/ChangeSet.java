package org.ctoolkit.agent.dataset;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

import java.util.ArrayList;

/**
 * Change set change descriptors.
 *
 * @author <a href="mailto:aurel.medvegy@ctoolkit.org">Aurel Medvegy</a>
 */
@Root( name = "changeset" )
public class ChangeSet
{
    @Attribute( required = false )
    private Long version;

    @Attribute( required = false )
    private Long subVersion;

    @Attribute
    private String author;

    @Attribute( required = false )
    private Boolean executeAlways;

    @Attribute( required = false )
    private Boolean completeDataSet;

    @Attribute( required = false )
    private String comment;

    @Element( required = false )
    private ChangeSetModel model;

    @Element( required = false )
    private ChangeSetEntities entities;

    /**
     * Default constructor
     */
    public ChangeSet()
    {
    }

    /**
     * Creates an empty, initialized change set.
     *
     * @return the empty initialized change set instance
     */
    public static ChangeSet createChangeSet()
    {
        // initialize the model section
        ChangeSetModel model = new ChangeSetModel();
        // initialize kind operations
        model.setKindPropOps( new ArrayList<KindPropOp>() );
        // initialize kindprop operations
        model.setKindOps( new ArrayList<KindOp>() );

        // entities section
        ChangeSetEntities changeSetEntities = new ChangeSetEntities();
        changeSetEntities.setEntities( new ArrayList<ChangeSetEntity>() );

        // create a change set
        ChangeSet changeSet = new ChangeSet();
        changeSet.setEntities( changeSetEntities );
        changeSet.setModel( model );

        return changeSet;
    }

    /**
     * Creates an empty, initialized change set.
     *
     * @return the empty initialized change set instance
     */
    public static ChangeSet createChangeSet( Long version, String author )
    {
        // initialize the model section
        ChangeSetModel model = new ChangeSetModel();
        // initialize kind operations
        model.setKindPropOps( new ArrayList<KindPropOp>() );
        // initialize kindprop operations
        model.setKindOps( new ArrayList<KindOp>() );

        // entities section
        ChangeSetEntities ents = new ChangeSetEntities();
        ents.setEntities( new ArrayList<ChangeSetEntity>() );

        // create a change set
        ChangeSet changeSet = new ChangeSet();
        changeSet.setEntities( ents );
        changeSet.setModel( model );

        changeSet.setVersion( version );
        changeSet.setAuthor( author );

        return changeSet;
    }

    /**
     * Creates an empty, initialized change set
     *
     * @return the empty initialized change set instance
     */
    public static ChangeSet createChangeSet( Long version, Long subVersion, String author )
    {
        // initialize the model section
        ChangeSetModel model = new ChangeSetModel();
        // initialize kind operations
        model.setKindPropOps( new ArrayList<KindPropOp>() );
        // initialize kindprop operations
        model.setKindOps( new ArrayList<KindOp>() );

        // entities section
        ChangeSetEntities changeSetEntities = new ChangeSetEntities();
        changeSetEntities.setEntities( new ArrayList<ChangeSetEntity>() );

        // create a changeset
        ChangeSet changeSet = new ChangeSet();
        changeSet.setEntities( changeSetEntities );
        changeSet.setModel( model );

        changeSet.setVersion( version );
        changeSet.setSubVersion( subVersion );
        changeSet.setAuthor( author );

        return changeSet;
    }

    /**
     * Returns unique change set version.
     *
     * @return the change set version
     */
    public Long getVersion()
    {
        return version;
    }

    /**
     * Sets change set version, must be unique.
     *
     * @param version the change set version to be set
     */
    public void setVersion( Long version )
    {
        this.version = version;
    }

    /**
     * Returns author of the change set.
     *
     * @return the author
     */
    public String getAuthor()
    {
        return author;
    }

    /**
     * Sets name of the change set author.
     *
     * @param author the author to be set
     */
    public void setAuthor( String author )
    {
        this.author = author;
    }

    /**
     * Returns comment about the change set, optional.
     *
     * @return the comment
     */
    public String getComment()
    {
        return comment;
    }

    /**
     * Sets optional comment about the change set.
     *
     * @param comment the comment to be set
     */
    public void setComment( String comment )
    {
        this.comment = comment;
    }

    /**
     * Returns model change descriptor, optional.
     *
     * @return the model
     */
    public ChangeSetModel getModel()
    {
        return model;
    }

    /**
     * Sets optional model change descriptor.
     *
     * @param model the model to be set
     */
    public void setModel( ChangeSetModel model )
    {
        this.model = model;
    }

    /**
     * Returns the entity change descriptors, optional.
     *
     * @return the entities
     */
    public ChangeSetEntities getEntities()
    {
        return entities;
    }

    /**
     * Sets optional entity change descriptors.
     *
     * @param entities the entities to be set
     */
    public void setEntities( ChangeSetEntities entities )
    {
        this.entities = entities;
    }

    /**
     * Returns the boolean indication whether the change set is always executed or not.
     *
     * @return true to execute always
     */
    public Boolean isExecuteAlways()
    {
        return executeAlways;
    }

    /**
     * Sets the boolean indication whether the change set is always executed or not.
     *
     * @param executeAlways the executeAlways to be set
     */
    public void setExecuteAlways( Boolean executeAlways )
    {
        this.executeAlways = executeAlways;
    }

    /**
     * Returns the change set sub version, used by database import/export.
     * Note, there can be more exports for the same version of the data model.
     *
     * @return the sub version
     */
    public Long getSubVersion()
    {
        return subVersion;
    }

    /**
     * Sets the change set sub version, used by database import/export.
     *
     * @param subVersion the subVersion to be set
     */
    public void setSubVersion( Long subVersion )
    {
        this.subVersion = subVersion;
    }

    /**
     * Returns the boolean indication whether this change set contains complete data store dump or not.
     *
     * @return true if this change set has complete data store dump
     */
    public Boolean isCompleteDataSet()
    {
        return completeDataSet;
    }

    /**
     * Sets the boolean indication whether the change set contains complete data store dump or not.
     *
     * @param completeDataSet the completeDataSet to be set
     */
    public void setCompleteDataSet( Boolean completeDataSet )
    {
        this.completeDataSet = completeDataSet;
    }

    /**
     * Adds the change set entity.
     *
     * @param entity the change set entity to be added
     * @return this instance for chaining
     */
    public ChangeSet addEntity( ChangeSetEntity entity )
    {
        getEntities().getEntities().add( entity );
        return this;
    }

    /**
     * Returns true if this change set has entities.
     *
     * @return true if this change set has entities
     */
    public boolean hasEntities()
    {
        return null != entities && !getEntities().getEntities().isEmpty();
    }

    /**
     * Returns true if this change set has model change description.
     *
     * @return true if this change set has model change description
     */
    public boolean hasModel()
    {
        return null != model && ( model.hasKindOps() || model.hasKindPropOps() );
    }

    /**
     * Returns <tt>true</tt> if this change set does not contain any change description.
     *
     * @return true if this change set does not contain any change description
     */
    public boolean isEmpty()
    {
        return !hasEntities() && !hasModel();
    }

    @Override
    public String toString()
    {
        String ret = "Version " + getVersion() + ", created by " + getAuthor() + " (" + getComment() + ")";
        if ( hasModel() )
        {
            ret += "\n Models changes: " + getModel();
        }
        if ( hasEntities() )
        {
            ret += "\n Entity changes: " + getEntities();
        }
        return ret;
    }
}
