package org.ctoolkit.agent.service;

import org.ctoolkit.agent.dataset.processor.DataSetProcessor;

import javax.inject.Inject;
import javax.inject.Singleton;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * The default dataset (classpath:dataset directory) import servlet starter.
 *
 * @author <a href="mailto:aurel.medvegy@ctoolkit.org">Aurel Medvegy</a>
 */
@Singleton
public class DefaultDatasetImportStarter
        extends HttpServlet
{
    private static final long serialVersionUID = 1L;

    @SuppressWarnings( "NonSerializableFieldInSerializableClass" )
    private final DataSetProcessor processor;

    @Inject
    public DefaultDatasetImportStarter( DataSetProcessor processor )
    {
        this.processor = processor;
    }

    @Override
    protected void doGet( HttpServletRequest request, HttpServletResponse response )
            throws ServletException, IOException
    {
        response.setContentType( "text/html; charset=UTF-8" );

        processor.upgrade( 0L, 0L );

        response.getWriter().write( "The default dataset import has started ..." );
    }
}
