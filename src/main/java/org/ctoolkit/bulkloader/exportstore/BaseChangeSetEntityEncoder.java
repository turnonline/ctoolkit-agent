package org.ctoolkit.bulkloader.exportstore;

import org.ctoolkit.bulkloader.changesets.model.ChangeSetEntity;

/**
 * ChangeSet entity encoder
 *
 * @author <a href="mailto:medvegy@comvai.com">Aurel Medvegy</a>
 */
public abstract class BaseChangeSetEntityEncoder
        implements ChangeSetEntityEncoder
{

    /**
     * Persister used for transforming change set entity
     */
    private final ChangeSetEntityEncoder decoratedEncoder;

    /**
     * Default constructor
     * s* @param encoder
     */
    public BaseChangeSetEntityEncoder( ChangeSetEntityEncoder encoder )
    {
        decoratedEncoder = encoder;
    }

    /**
     * Method encodes a change set entity into byte array
     *
     * @param cse entity to encode
     * @return entity encoded into byte array, or null
     */
    public byte[] toByteArray( final ChangeSetEntity cse )
    {
        return decoratedEncoder.toByteArray( cse );
    }

    /**
     * Method decodes a change set entity from byte array
     *
     * @param ch byte array containing encoded entity
     * @return decoded change set entity, or null
     */
    public ChangeSetEntity toChangeSetEntity( final byte[] ch )
    {
        return decoratedEncoder.toChangeSetEntity( ch );
    }
}
