package org.ctoolkit.agent;

import com.google.common.eventbus.EventBus;
import com.google.inject.AbstractModule;
import com.google.inject.assistedinject.FactoryModuleBuilder;
import com.google.inject.name.Names;
import com.googlecode.objectify.ObjectifyService;
import org.ctoolkit.agent.dataset.processor.DataSetProcessor;
import org.ctoolkit.agent.dataset.processor.impl.DataSetProcessorBean;
import org.ctoolkit.agent.dataset.processor.impl.UpgradeTask;
import org.ctoolkit.agent.dataset.reader.ChangeSetVersionService;
import org.ctoolkit.agent.dataset.reader.DataSetReadingStrategy;
import org.ctoolkit.agent.dataset.reader.DataSetReadingStrategyFactory;
import org.ctoolkit.agent.dataset.reader.impl.ChangeSetVersionImpl;
import org.ctoolkit.agent.dataset.reader.impl.DataSetMultipleXmlReader;
import org.ctoolkit.agent.datastore.DataStore;
import org.ctoolkit.agent.datastore.bigtable.BigTableDataStore;
import org.ctoolkit.agent.restapi.EventBusSubscription;
import org.ctoolkit.agent.restapi.resource.DataSetExport;
import org.ctoolkit.agent.restapi.resource.DataSetJob;
import org.ctoolkit.agent.restapi.resource.DataSetUpgrade;

import javax.inject.Singleton;


/**
 * @author <a href="mailto:aurel.medvegy@ctoolkit.org">Aurel Medvegy</a>
 */
public class AgentModule
        extends AbstractModule
{
    @Override
    protected void configure()
    {
        install( new FactoryModuleBuilder()
                .implement( DataSetReadingStrategy.class, Names.named( "DataSets" ), DataSetMultipleXmlReader.class )
                .build( DataSetReadingStrategyFactory.class ) );

        bind( DataSetProcessor.class ).to( DataSetProcessorBean.class ).in( Singleton.class );
        bind( ChangeSetVersionService.class ).to( ChangeSetVersionImpl.class ).in( Singleton.class );
        bind( DataStore.class ).to( BigTableDataStore.class ).in( Singleton.class );
        // default event bus
        bind( EventBus.class ).in( Singleton.class );
        bind( EventBusSubscription.class ).in( Singleton.class );

        requestStaticInjection( UpgradeTask.class );

        ObjectifyService.register( DataSetJob.class );
        ObjectifyService.register( DataSetUpgrade.class );
        ObjectifyService.register( DataSetExport.class );
    }
}
