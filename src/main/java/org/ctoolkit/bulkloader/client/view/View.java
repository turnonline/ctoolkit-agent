package org.ctoolkit.bulkloader.client.view;

import com.google.gwt.user.client.ui.Composite;
import com.google.web.bindery.event.shared.EventBus;
import org.ctoolkit.bulkloader.client.AppMessages;
import org.ctoolkit.bulkloader.client.ResourceBundle;
import org.ctoolkit.bulkloader.client.Styles;

/**
 * <p>Basic view implementation</p>
 * <p>View is an user interface component that displays data (a model) and routes user commands (events) to the
 * presenter to act upon that data.</p>
 *
 * @author <a href="mailto:medvegy@foundation.sk>Aurel Medvegy</a>"
 */
public class View
        extends Composite
        implements IView
{
    protected AppMessages messages = AppMessages.INSTANCE;

    protected Styles styles = ResourceBundle.INSTANCE.styles();

    protected EventBus eventBus;

    public View( EventBus eventBus )
    {
        this.eventBus = eventBus;
    }

    @Override
    public void hide()
    {
    }

    @Override
    public void show()
    {
    }
}
