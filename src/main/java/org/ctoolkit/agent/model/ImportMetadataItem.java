package org.ctoolkit.agent.model;

import com.googlecode.objectify.Ref;
import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Ignore;
import com.googlecode.objectify.annotation.OnSave;
import com.googlecode.objectify.annotation.Parent;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Import metadata item entity
 *
 * @author <a href="mailto:pohorelec@comvai.com">Jozef Pohorelec</a>
 */
@Entity
public class ImportMetadataItem
        extends BaseEntity
{
    @Parent
    private Ref<ImportMetadata> importMetadataRef;

    @Ignore
    private ImportMetadata importMetadata;

    private byte[] xml;

    public ImportMetadataItem()
    {
    }

    public ImportMetadataItem( ImportMetadata importMetadata )
    {
        this.importMetadata = importMetadata;
    }

    public ImportMetadata getImportMetadata()
    {
        if ( importMetadata == null )
        {
            importMetadata = importMetadataRef.get();
        }
        return importMetadata;
    }

    public byte[] getXml()
    {
        return xml;
    }

    public void setXml( byte[] xml )
    {
        this.xml = xml;
    }

    @OnSave
    private void updateObjectifyRefs()
    {
        if ( importMetadataRef == null )
        {
            checkNotNull( importMetadata, "ImportMetadata is mandatory to create a new persisted ImportMetadataItem!" );
            importMetadataRef = Ref.create( importMetadata );
        }
    }

    @Override
    public String toString()
    {
        return "ImportMetadataItem{" +
                "xml.length=" + xml.length +
                "} " + super.toString();
    }
}
