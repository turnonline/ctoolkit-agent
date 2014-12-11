package org.ctoolkit.bulkloader.client;

import com.google.gwt.activity.shared.ActivityManager;
import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.place.shared.Place;
import com.google.gwt.place.shared.PlaceHistoryHandler;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.ui.RootLayoutPanel;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.SimplePanel;
import org.ctoolkit.bulkloader.client.place.BulkLoaderPlaceHistoryMapper;
import org.ctoolkit.bulkloader.client.place.MainPlace;

/**
 * @author <a href="mailto:medvegy@comvai.com>Aurel Medvegy</a>"
 */
public class BulkLoader
        implements EntryPoint
{

    private static final String LOADING_ID = "loading";

    private Place defaultPlace = new MainPlace();

    private SimplePanel container = new SimplePanel();

    private Injector injector = GWT.create( Injector.class );

    /**
     * @see com.google.gwt.core.client.EntryPoint#onModuleLoad()
     */
    @Override
    public void onModuleLoad()
    {
        // ensure that styles will be inject properly (otherwise styles just wont work)
        ResourceBundle.INSTANCE.styles().ensureInjected();

        // remove 'Loading' panel
        RootPanel.getBodyElement().removeChild( DOM.getElementById( LOADING_ID ) );

        // create activity manager and put container in it
        ActivityManager activityManager = injector.getActivityManager();
        activityManager.setDisplay( container );

        // Start PlaceHistoryHandler with our PlaceHistoryMapper
        BulkLoaderPlaceHistoryMapper historyMapper = GWT.create( BulkLoaderPlaceHistoryMapper.class );
        PlaceHistoryHandler historyHandler = new PlaceHistoryHandler( historyMapper );
        historyHandler.register( injector.getPlaceController(), injector.getEventBus(), defaultPlace );

        // Goes to place represented on URL or default place
        historyHandler.handleCurrentHistory();

        // add busy indicator on top of page
        RootLayoutPanel.get().add( injector.getBusyIndicatorPanel() );

        // crate container for page
        RootLayoutPanel.get().add( container );
    }
}
