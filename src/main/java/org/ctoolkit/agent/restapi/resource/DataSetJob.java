package org.ctoolkit.agent.restapi.resource;

import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;

import java.util.Date;

/**
 * The basic resource to model operations on data set.
 *
 * @author <a href="mailto:aurel.medvegy@ctoolkit.org">Aurel Medvegy</a>
 */
@Entity
abstract class DataSetJob
{
    @Id
    private Long id;

    private Long dataSetId;

    private boolean completed;

    private Date completedAt;
}
