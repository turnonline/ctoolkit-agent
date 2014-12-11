package org.ctoolkit.bulkloader.exportstore;

import org.ctoolkit.bulkloader.changesets.model.ChangeSetEntity;
import org.simpleframework.xml.core.Persister;

import java.io.StringWriter;
import java.util.logging.Logger;

/**
 * Xml ChangeSet entity encoder
 *
 * @author <a href="mailto:medvegy@comvai.com">Aurel Medvegy</a>
 */
public class XmlChangeSetEntityEncoder
        implements ChangeSetEntityEncoder
{

    /**
     * Logger for this class
     */
    private final static Logger logger = Logger.getLogger( XmlChangeSetEntityEncoder.class.getName() );

    /**
     * Persister used for transforming change set entity
     */
    private final Persister persister;

    /**
     * Default constructor
     */
    public XmlChangeSetEntityEncoder()
    {
        persister = new Persister();
    }

    /**
     * Method encodes a change set entity into byte array
     *
     * @param cse entity to encode
     * @return entity encoded into byte array, or null
     */
    public byte[] toByteArray( final ChangeSetEntity cse )
    {
        StringWriter sw = new StringWriter();
        try
        {
            persister.write( cse, sw );
            return sw.getBuffer().toString().getBytes();
        }
        catch ( Exception ignored )
        {
            logger.severe( ignored.getMessage() );
        }
        return null;
    }

    /**
     * Method decodes a change set entity from byte array
     *
     * @param ch byte array containing encoded entity
     * @return decoded change set entity, or null
     */
    public ChangeSetEntity toChangeSetEntity( final byte[] ch )
    {
        try
        {
            return persister.read( ChangeSetEntity.class, new String( ch ) );
        }
        catch ( Exception ignored )
        {
            logger.severe( ignored.getMessage() );
        }
        return null;
    }
}
