package org.ctoolkit.bulkloader.client.view;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.web.bindery.event.shared.EventBus;
import org.ctoolkit.bulkloader.client.event.UpgradeEvent;
import org.ctoolkit.bulkloader.client.presenter.MainPresenter;

import javax.inject.Inject;

/**
 * @author <a href="mailto:medvegy@comvai.com>Aurel Medvegy</a>"
 */
public class MainView
        extends View
        implements MainPresenter.IView
{
    private final FlowPanel panel;

    private Button button;

    @Inject
    public MainView( EventBus eventBus )
    {
        super( eventBus );
        panel = new FlowPanel();

        initWidget( panel );
    }

    public void init()
    {
        ButtonClickHandler handler = new ButtonClickHandler();
        button = new Button( messages.btnUpgrade() );
        button.addClickHandler( handler );
        panel.add( button );
    }

    private class ButtonClickHandler
            implements ClickHandler
    {
        @Override
        public void onClick( ClickEvent event )
        {
            Widget source = ( Widget ) event.getSource();

            if ( source == button )
            {
                eventBus.fireEvent( new UpgradeEvent() );
            }
        }
    }
}
