package org.ctoolkit.bulkloader.client;

import com.google.gwt.core.client.GWT;
import com.google.gwt.i18n.client.Messages;

/**
 * @author <a href="mailto:medvegy@comvai.com>Aurel Medvegy</a>"
 */
public interface AppMessages
        extends Messages
{
    public static final AppMessages INSTANCE = GWT.create( AppMessages.class );

    @Key( "btn.upgrade" )
    String btnUpgrade();

    @Key( "title.bulkloader" )
    String titleBulkloader();
}
