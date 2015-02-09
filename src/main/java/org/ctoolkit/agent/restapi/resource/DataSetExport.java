package org.ctoolkit.agent.restapi.resource;

import com.googlecode.objectify.annotation.Subclass;

/**
 * The job resource to describe data set export job details.
 *
 * @author <a href="mailto:aurel.medvegy@ctoolkit.org">Aurel Medvegy</a>
 */
@Subclass( index = true )
public class DataSetExport
        extends DataSetJob
{
    private static final long serialVersionUID = 1L;

    public DataSetExport()
    {
    }

    public DataSetExport( Long id )
    {
        super( id );
    }
}
