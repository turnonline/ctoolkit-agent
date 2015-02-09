package org.ctoolkit.agent.dataset.processor;

import org.ctoolkit.agent.common.AgentException;

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
     * The asynchronous execution of the upgrade of the current datastore by applying of the change set (given Id).
     * The notification {@link UpgradeCompletedEvent} will be fired (through {@link com.google.common.eventbus.EventBus})
     * once upgrade has completed.
     *
     * @param dataSetId      the Id of the data set as a source
     * @param notificationId the notification Id to pass back once upgrade has completed
     *                       as part of {@link UpgradeCompletedEvent}
     */
    void upgrade( @Nonnull Long dataSetId, @Nullable Long notificationId );

    /**
     * The synchronous upgrade of the current datastore by reading given data set.
     *
     * @param progress the progress info as a upgrade starting point
     * @param dataSet  the data set as a source for upgrade
     * @return the current progress info, suitable for upgrade chaining
     */
    ProgressInfo upgrade( @Nonnull ProgressInfo progress, @Nonnull DataSet dataSet )
            throws AgentException;
}
