package org.ctoolkit.agent;

import com.comvai.test.gae.ServiceConfigModule;
import com.google.appengine.tools.development.testing.LocalBlobstoreServiceTestConfig;
import com.google.appengine.tools.development.testing.LocalDatastoreServiceTestConfig;
import com.google.appengine.tools.development.testing.LocalMemcacheServiceTestConfig;
import com.google.appengine.tools.development.testing.LocalServiceTestHelper;
import com.google.appengine.tools.development.testing.LocalTaskQueueTestConfig;

import java.util.concurrent.TimeUnit;

/**
 * @author <a href="mailto:aurel.medvegy@ctoolkit.org">Aurel Medvegy</a>
 */
public class UseCaseEnvironment
        extends ServiceConfigModule
{
    // Unlike CountDownLatch, TaskCountDownlatch lets us reset.
    final LocalTaskQueueTestConfig.TaskCountDownLatch latch = new LocalTaskQueueTestConfig.TaskCountDownLatch( 1 );

    public UseCaseEnvironment()
    {
        construct( new LocalServiceTestHelper(
                new LocalMemcacheServiceTestConfig(),
                new LocalDatastoreServiceTestConfig().setDefaultHighRepJobPolicyUnappliedJobPercentage( 0 ),
                new LocalBlobstoreServiceTestConfig(),
                new LocalTaskQueueTestConfig().setQueueXmlPath( "src/main/webapp/WEB-INF/queue.xml" )
                        .setDisableAutoTaskExecution( false )
                        .setCallbackClass( LocalTaskQueueTestConfig.DeferredTaskCallback.class ) ) );
    }

    @Override
    public void configureTestBinder()
    {
        // setting the SystemProperty.Environment.Value.Development
        System.setProperty( "com.google.appengine.runtime.environment", "Development" );
    }

    protected void await( long seconds ) throws InterruptedException
    {
        latch.await( seconds, TimeUnit.MILLISECONDS );
    }
}
