package org.ctoolkit.bulkloader.changesets.model;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

import java.util.ArrayList;

/**
 * Change set holding change descriptors
 *
 * @author <a href="mailto:medvegy@comvai.com">Aurel Medvegy</a>
 */
@Root( name = "changeset" )
public class ChangeSet
{

    /**
     * Change set version, should be unique
     */
    @Attribute( required = false )
    private Long version;

    /**
     * Change set sub version, used by database import/export
     * There can be more exports for the same version of the
     * data model
     */
    @Attribute( required = false )
    private Long subVersion;

    /**
     * Name of the change sets author
     */
    @Attribute
    private String author;

    /**
     * If flag is set, the change set is always executed
     */
    @Attribute( required = false )
    private Boolean executeAlways;

    /**
     * If flag is set, it means, the change set contains complete data store dump
     */
    @Attribute( required = false )
    private Boolean completeDataSet;

    /**
     * Comment about the change set, optional
     */
    @Attribute( required = false )
    private String comment;

    /**
     * Optional model change descriptor
     */
    @Element( required = false )
    private ChangeSetModel model;

    /**
     * Optional entity change descriptor
     */
    @Element( required = false )
    private ChangeSetEntities entities;

    /**
     * Default constructor
     */
    public ChangeSet()
    {
    }

    /**
     * Method used by Test cases
     *
     * @param version
     * @param subVersion
     * @param author
     * @param executeAlways
     * @param completeDataSet
     * @param comment
     * @param model
     * @param entities
     */
    public ChangeSet( Long version, Long subVersion, String author,
                      Boolean executeAlways, Boolean completeDataSet, String comment,
                      ChangeSetModel model, ChangeSetEntities entities )
    {
        super();
        this.version = version;
        this.subVersion = subVersion;
        this.author = author;
        this.executeAlways = executeAlways;
        this.completeDataSet = completeDataSet;
        this.comment = comment;
        this.model = model;
        this.entities = entities;
    }

    /**
     * Method creates an empty, initialized change set
     *
     * @return
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

        // create a changeeset
        ChangeSet changeSet = new ChangeSet();
        changeSet.setEntities( changeSetEntities );
        changeSet.setModel( model );

        return changeSet;
    }

    /**
     * Method creates an empty, initialized changeset
     *
     * @return
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

        // create a changeeset
        ChangeSet changeSet = new ChangeSet();
        changeSet.setEntities( ents );
        changeSet.setModel( model );

        changeSet.setVersion( version );
        changeSet.setAuthor( author );

        return changeSet;
    }

    /**
     * Method creates an empty, initialized changeset
     *
     * @return
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
     * @return the author
     */
    public String getAuthor()
    {
        return author;
    }

    /**
     * @param author the author to set
     */
    public void setAuthor( String author )
    {
        this.author = author;
    }

    /**
     * @return the comment
     */
    public String getComment()
    {
        return comment;
    }

    /**
     * @param comment the comment to set
     */
    public void setComment( String comment )
    {
        this.comment = comment;
    }

    /**
     * @return the model
     */
    public ChangeSetModel getModel()
    {
        return model;
    }

    /**
     * @param model the model to set
     */
    public void setModel( ChangeSetModel model )
    {
        this.model = model;
    }

    /**
     * @return the entities
     */
    public ChangeSetEntities getEntities()
    {
        return entities;
    }

    /**
     * @param entities the entities to set
     */
    public void setEntities( ChangeSetEntities entities )
    {
        this.entities = entities;
    }

    /**
     * @return the executeAlways
     */
    public Boolean getExecuteAlways()
    {
        return executeAlways;
    }

    /**
     * @param executeAlways the executeAlways to set
     */
    public void setExecuteAlways( Boolean executeAlways )
    {
        this.executeAlways = executeAlways;
    }

    /**
     * @return the subVersion
     */
    public Long getSubVersion()
    {
        return subVersion;
    }

    /**
     * @param subVersion the subVersion to set
     */
    public void setSubVersion( Long subVersion )
    {
        this.subVersion = subVersion;
    }

    /**
     * @return the completeDataSet
     */
    public Boolean getCompleteDataSet()
    {
        return completeDataSet;
    }

    /**
     * @param completeDataSet the completeDataSet to set
     */
    public void setCompleteDataSet( Boolean completeDataSet )
    {
        this.completeDataSet = completeDataSet;
    }

    /**
     * @param entity
     */
    public ChangeSet addEntity( ChangeSetEntity entity )
    {
        getEntities().getEntities().add( entity );
        return this;
    }

    /**
     * @return true if the ChangeSet has entities
     */
    public boolean hasEntities()
    {
        return null != entities && !getEntities().getEntities().isEmpty();
    }

    /**
     * @return true if the ChangeSet has Model change description
     */
    public boolean hasModel()
    {
        return null != model && ( model.hasKindOps() || model.hasKindPropOps() );
    }

    /**
     * @return true if the change set does not contain any change description
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
