package org.ctoolkit.bulkloader.client.presenter;

import com.comvai.gwt.client.event.CachedEventBus;
import com.comvai.gwt.client.event.RPCInEvent;
import com.comvai.gwt.client.event.RPCOutEvent;
import com.comvai.gwt.client.presenter.Presenter;
import com.google.gwt.place.shared.PlaceController;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.AcceptsOneWidget;
import com.google.web.bindery.event.shared.EventBus;
import org.ctoolkit.bulkloader.client.AppMessages;
import org.ctoolkit.bulkloader.client.view.IView;

/**
 * @author <a href="mailto:medvegy@comvai.com">Aurel Medvegy</a>
 */
public abstract class BasePresenter<V extends IView>
        extends Presenter<V, CachedEventBus>
{
    private static IView currentView;

    protected AppMessages messages = AppMessages.INSTANCE;

    private AcceptsOneWidget root;

    public BasePresenter( CachedEventBus eventBus, V view, PlaceController placeController )
    {
        super( eventBus, view, placeController );
    }

    @Override
    public final void start( AcceptsOneWidget panel, EventBus eventBus )
    {
        eventBus.fireEvent( new RPCOutEvent() );

        root = panel;

        // call onBeforeBackingObject which will hide content
        onBeforeBackingObject();

        // add view to the root of page (NOTE: panel is root of the page)
        panel.setWidget( currentView == null ? view : currentView );

        // call backing object method
        onBackingObject();

    }

    @Override
    public void onStop()
    {
        currentView = view;
    }

    /**
     * <p>This method is called before {@link com.comvai.gwt.client.presenter.Presenter#onBackingObject()} method.</p>
     * <p>It just hides visible view, until data are completely loaded from backend and view is filled with it.</p>
     */
    protected void onBeforeBackingObject()
    {
        view.hide();
    }

    /**
     * <p>Call this method when backing object is fully loaded from backend and its data are filled to view.</p>
     * <p>You need to call this method by hand because of asynchronous behavior of getting backing object.</p>
     * <p>Method just show view with prepared data and view components.</p>
     */
    protected void onAfterBackingObject()
    {
        view.show();

        this.root.setWidget( view );
        eventBus.fireEvent( new RPCInEvent() );
    }

    /**
     * <p>Set title of page. Call this method in {@link com.comvai.gwt.client.presenter.Presenter#onBackingObject()} method to ensure
     * that title will be rendered on every page correctly.</p>
     *
     * @param titleText text which will be shown in the browser window title
     */
    public void setTitle( String titleText )
    {
        Window.setTitle( titleText );
    }
}
