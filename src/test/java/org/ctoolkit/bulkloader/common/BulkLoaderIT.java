package org.ctoolkit.bulkloader.common;

import com.google.guiceberry.junit4.GuiceBerryRule;
import org.ctoolkit.bulkloader.UseCaseEnvironment;
import org.junit.Rule;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;

public class BulkLoaderIT
        extends UseCaseEnvironment
{
    private static final Logger log = LoggerFactory.getLogger( BulkLoaderIT.class );

    @Rule
    public final GuiceBerryRule guiceBerry = new GuiceBerryRule( BulkLoaderIT.class );

    @Inject
    private BulkLoader bulkLoader;

    @Test
    public void doImport() throws Exception
    {
        log.info( "bulkLoader = " + bulkLoader );
    }

    @Test
    public void doExport() throws Exception
    {
        log.info( "bulkLoader = " + bulkLoader );
    }

    @Test
    public void doUpgrade() throws Exception
    {
        log.info( "bulkLoader = " + bulkLoader );
    }
}