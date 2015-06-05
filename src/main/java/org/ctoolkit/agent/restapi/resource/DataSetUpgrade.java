package org.ctoolkit.agent.restapi.resource;

import com.googlecode.objectify.annotation.EntitySubclass;

/**
 * The job resource to describe data set upgrade job details.
 *
 * @author <a href="mailto:aurel.medvegy@ctoolkit.org">Aurel Medvegy</a>
 */
@EntitySubclass( index = true )
public class DataSetUpgrade
        extends DataSetJob
{
    private static final long serialVersionUID = 1L;

    public DataSetUpgrade()
    {
    }

    public DataSetUpgrade( Long id )
    {
        super( id );
    }
}
