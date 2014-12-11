package org.ctoolkit.bulkloader.client;

import com.comvai.gwt.client.component.BusyIndicatorPanel;
import com.comvai.gwt.client.event.CachedEventBus;
import com.google.gwt.activity.shared.ActivityManager;
import com.google.gwt.inject.client.GinModules;
import com.google.gwt.inject.client.Ginjector;
import com.google.gwt.place.shared.PlaceController;

/**
 * @author <a href="mailto:medvegy@comvai.com>Aurel Medvegy</a>"
 */
@GinModules( Module.class )
public interface Injector
        extends Ginjector
{
    BusyIndicatorPanel getBusyIndicatorPanel();

    PlaceController getPlaceController();

    CachedEventBus getEventBus();

    ActivityManager getActivityManager();

}
