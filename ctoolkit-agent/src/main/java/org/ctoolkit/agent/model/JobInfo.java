package org.ctoolkit.agent.model;

/**
 * Job info
 *
 * @author <a href="mailto:jozef.pohorelec@ctoolkit.org">Jozef Pohorelec</a>
 */
public abstract class JobInfo
{
    private String id;

    private String mapReduceJobId;

    private int totalItems;

    private int processedItems;

    private int processedErrorItems;

    private JobState state;

    private String stackTrace;

    public String getId()
    {
        return id;
    }

    public void setId( String id )
    {
        this.id = id;
    }

    public String getMapReduceJobId()
    {
        return mapReduceJobId;
    }

    public void setMapReduceJobId( String mapReduceJobId )
    {
        this.mapReduceJobId = mapReduceJobId;
    }

    public int getTotalItems()
    {
        return totalItems;
    }

    public void setTotalItems( int totalItems )
    {
        this.totalItems = totalItems;
    }

    public int getProcessedItems()
    {
        return processedItems;
    }

    public void setProcessedItems( int processedItems )
    {
        this.processedItems = processedItems;
    }

    public int getProcessedErrorItems()
    {
        return processedErrorItems;
    }

    public void setProcessedErrorItems( int processedErrorItems )
    {
        this.processedErrorItems = processedErrorItems;
    }

    public JobState getState()
    {
        return state;
    }

    public void setState( JobState state )
    {
        this.state = state;
    }

    public String getStackTrace()
    {
        return stackTrace;
    }

    public void setStackTrace( String stackTrace )
    {
        this.stackTrace = stackTrace;
    }

    @Override
    public String toString()
    {
        return "JobInfo{" +
                "id='" + id + '\'' +
                ", mapReduceJobId='" + mapReduceJobId + '\'' +
                ", totalItems=" + totalItems +
                ", processedItems=" + processedItems +
                ", processedErrorItems=" + processedErrorItems +
                ", state=" + state +
                ", stackTrace='" + stackTrace + '\'' +
                '}';
    }
}