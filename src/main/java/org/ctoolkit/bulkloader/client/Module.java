package org.ctoolkit.bulkloader.client;

import com.comvai.gwt.client.CGwtGinModule;
import com.comvai.gwt.client.event.CachedEventBus;
import com.google.gwt.activity.shared.ActivityMapper;
import com.google.gwt.inject.client.AbstractGinModule;
import com.google.web.bindery.event.shared.EventBus;
import org.ctoolkit.bulkloader.client.presenter.MainPresenter;
import org.ctoolkit.bulkloader.client.view.MainView;

import javax.inject.Singleton;

/**
 * @author <a href="mailto:medvegy@comvai.com>Aurel Medvegy</a>"
 */
public class Module
        extends AbstractGinModule
{

    @Override
    protected void configure()
    {
        // bind event bus
        bind( EventBus.class ).to( CachedEventBus.class );

        // install CGwtGinModule
        install( new CGwtGinModule() );

        // bind main view
        bind( MainPresenter.IView.class ).to( MainView.class ).in( Singleton.class );

        // bind application controller
        bind( ActivityMapper.class ).to( AppController.class );
    }
}
