package org.ctoolkit.bulkloader.client.event;

import com.google.gwt.event.shared.EventHandler;

/**
 * @author <a href="mailto:medvegy@comvai.com">Aurel Medvegy</a>
 */
public interface UpgradeEventHandler
        extends EventHandler
{
    void onSave( UpgradeEvent event );
}
