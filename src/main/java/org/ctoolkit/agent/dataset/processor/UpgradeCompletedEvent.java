package org.ctoolkit.agent.dataset.processor;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * The event to be fired once upgrade has completed.
 *
 * @author <a href="mailto:aurel.medvegy@ctoolkit.org">Aurel Medvegy</a>
 */
public class UpgradeCompletedEvent
{
    private final DataSet dataSet;

    private final Long notificationId;

    public UpgradeCompletedEvent( @Nonnull DataSet dataSet, @Nullable Long notificationId )
    {
        this.dataSet = checkNotNull( dataSet );
        this.notificationId = notificationId;
    }

    public DataSet getDataSet()
    {
        return dataSet;
    }

    public Long getNotificationId()
    {
        return notificationId;
    }
}
