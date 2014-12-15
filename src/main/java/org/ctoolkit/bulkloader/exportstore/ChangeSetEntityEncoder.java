package org.ctoolkit.bulkloader.exportstore;

import org.ctoolkit.bulkloader.changesets.model.ChangeSetEntity;

/**
 * ChangeSet entity encoder interface
 *
 * @author <a href="mailto:medvegy@comvai.com">Aurel Medvegy</a>
 */
public interface ChangeSetEntityEncoder
{

    public byte[] toByteArray( final ChangeSetEntity cse );

    public ChangeSetEntity toChangeSetEntity( final byte[] ch );
}
