package org.ctoolkit.bulkloader.client.presenter;

import com.comvai.gwt.client.event.CachedEventBus;
import com.comvai.gwt.common.command.BusyIndicatorCallback;
import com.google.gwt.core.shared.GWT;
import com.google.gwt.place.shared.PlaceController;
import org.ctoolkit.bulkloader.client.command.UpgradeAction;
import org.ctoolkit.bulkloader.client.command.UpgradeResponse;
import org.ctoolkit.bulkloader.client.event.UpgradeEvent;
import org.ctoolkit.bulkloader.client.event.UpgradeEventHandler;

/**
 * @author <a href="mailto:medvegy@comvai.com>Aurel Medvegy</a>"
 */
public class MainPresenter
        extends BasePresenter<MainPresenter.IView>
{
    @javax.inject.Inject
    public MainPresenter( CachedEventBus eventBus, IView view, PlaceController placeController )
    {
        super( eventBus, view, placeController );
    }

    @Override
    public void onBackingObject()
    {
        setTitle( messages.titleBulkloader() );
        view.init();
        onAfterBackingObject();
    }

    @Override
    public void bind()
    {
        super.bind();

        eventBus.addHandler( UpgradeEvent.TYPE, new UpgradeEventHandler()
        {
            @Override
            public void onSave( UpgradeEvent event )
            {
                eventBus.execute( new UpgradeAction(), new BusyIndicatorCallback<UpgradeResponse>( eventBus )
                {
                    @Override
                    public void onResponse( UpgradeResponse response )
                    {
                        GWT.log( "onResponse: " + response );
                    }

                    @Override
                    public void onException( Throwable throwable )
                    {
                        GWT.log( "onException" );
                    }
                } );
            }
        } );
    }

    public interface IView
            extends org.ctoolkit.bulkloader.client.view.IView
    {
        void init();
    }
}
