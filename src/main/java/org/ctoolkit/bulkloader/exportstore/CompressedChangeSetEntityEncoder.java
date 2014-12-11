package org.ctoolkit.bulkloader.exportstore;

import org.ctoolkit.bulkloader.changesets.model.ChangeSetEntity;
import org.ctoolkit.bulkloader.utils.Compressor;
import org.ctoolkit.bulkloader.utils.ZIPCompressor;

/**
 * Compressed changeSet entity encoder
 *
 * @author <a href="mailto:medvegy@comvai.com">Aurel Medvegy</a>
 */
public class CompressedChangeSetEntityEncoder
        extends BaseChangeSetEntityEncoder
{

    /**
     * Compressor used for compressing data
     */
    private final Compressor compressor;

    public CompressedChangeSetEntityEncoder( ChangeSetEntityEncoder encoder )
    {
        super( encoder );
        this.compressor = new ZIPCompressor();
    }

    @Override
    public byte[] toByteArray( ChangeSetEntity cse )
    {
        return compressor.compress( super.toByteArray( cse ) );
    }

    @Override
    public ChangeSetEntity toChangeSetEntity( byte[] ch )
    {
        return super.toChangeSetEntity( compressor.inflate( ch ) );
    }
}
