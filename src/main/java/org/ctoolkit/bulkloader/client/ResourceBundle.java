package org.ctoolkit.bulkloader.client;

/**
 * @author <a href="mailto:medvegy@comvai.com>Aurel Medvegy</a>"
 */

import com.google.gwt.core.client.GWT;
import com.google.gwt.resources.client.ClientBundle;

public interface ResourceBundle
        extends ClientBundle
{

    public static final ResourceBundle INSTANCE = GWT.create( ResourceBundle.class );

    @Source( "Styles.css" )
    public Styles styles();

}
