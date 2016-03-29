package org.ctoolkit.agent.model;

import com.googlecode.objectify.Ref;
import com.googlecode.objectify.annotation.Ignore;
import com.googlecode.objectify.annotation.OnSave;
import com.googlecode.objectify.annotation.Parent;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Base metadata item
 *
 * @author <a href="mailto:pohorelec@comvai.com">Jozef Pohorelec</a>
 * @see {@link ImportMetadataItem}
 */
public abstract class BaseMetadataItem<PARENT extends BaseMetadata>
        extends BaseEntity
{
    @Parent
    private Ref<PARENT> metadataRef;

    @Ignore
    private PARENT metadata;

    private byte[] xml;

    private JobState state;

    public BaseMetadataItem()
    {
    }

    public BaseMetadataItem( PARENT metadata )
    {
        this.metadata = metadata;
    }

    public PARENT getMetadata()
    {
        if ( metadata == null )
        {
            metadata = metadataRef.get();
        }
        return metadata;
    }

    public byte[] getXml()
    {
        return xml;
    }

    public void setXml( byte[] xml )
    {
        this.xml = xml;
    }

    public JobState getState()
    {
        return state;
    }

    public void setState( JobState state )
    {
        this.state = state;
    }

    @OnSave
    private void updateObjectifyRefs()
    {
        if ( metadataRef == null )
        {
            checkNotNull( metadata, "Metadata is mandatory to create a new persisted MetadataItem!" );
            metadataRef = Ref.create( metadata );
        }
    }

    @Override
    public String toString()
    {
        return "MetadataItem{" +
                "xml.length=" + (xml != null ? xml.length : null) +
                ", state=" + state +
                "} " + super.toString();
    }
}
