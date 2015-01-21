package org.ctoolkit.agent.dataset.processor;

import com.google.common.util.concurrent.FutureCallback;
import org.ctoolkit.agent.common.AgentException;
import org.ctoolkit.agent.dataset.DataSet;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * The datastore data set processor engine.
 *
 * @author <a href="mailto:aurel.medvegy@ctoolkit.org">Aurel Medvegy</a>
 */
public interface DataSetProcessor
{
    /**
     * The asynchronous execution of the upgrade of the current datastore by the given change set.
     *
     * @param dataSetId the Id of the data set as a source
     * @param callback  the callback as notification about upgrade result
     */
    void upgrade( @Nonnull Long dataSetId, @Nullable FutureCallback<DataSet> callback );

    /**
     * The synchronous upgrade of the current datastore by reading given data set.
     *
     * @param progress the progress info as a upgrade starting point
     * @param dataSet  the data set as a source for upgrade
     * @return the current progress info, suitable upgrade chaining
     */
    ProgressInfo upgrade( @Nonnull ProgressInfo progress, @Nonnull DataSet dataSet )
            throws AgentException;
}
