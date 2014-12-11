package org.ctoolkit.bulkloader.facade;

import com.comvai.gwt.server.CommandRemoteServiceServlet;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class CommandRemoteServlet
        extends CommandRemoteServiceServlet
{
    private static final long serialVersionUID = 1L;

    @Inject
    public CommandRemoteServlet( JobFacade facade )
    {
        super( facade );
    }
}