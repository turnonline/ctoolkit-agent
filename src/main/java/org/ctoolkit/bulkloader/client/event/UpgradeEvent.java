package org.ctoolkit.bulkloader.client.event;

import com.google.gwt.event.shared.GwtEvent;

/**
 * @author <a href="mailto:medvegy@comvai.com">Aurel Medvegy</a>
 */
public class UpgradeEvent
        extends GwtEvent<UpgradeEventHandler>
{
    public static Type<UpgradeEventHandler> TYPE = new Type<UpgradeEventHandler>();

    public Type<UpgradeEventHandler> getAssociatedType()
    {
        return TYPE;
    }

    protected void dispatch( UpgradeEventHandler handler )
    {
        handler.onSave( this );
    }
}
