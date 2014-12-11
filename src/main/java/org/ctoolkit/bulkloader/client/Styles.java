package org.ctoolkit.bulkloader.client;

import com.google.gwt.resources.client.CssResource;

/**
 * @author <a href="mailto:medvegy@comvai.com>Aurel Medvegy</a>"
 */
public interface Styles
        extends CssResource
{
    @ClassName( value = "busy-indicator" )
    String busyIndicator();

    @ClassName( "busy-indicator-hidden" )
    String busyIndicatorHidden();
}
