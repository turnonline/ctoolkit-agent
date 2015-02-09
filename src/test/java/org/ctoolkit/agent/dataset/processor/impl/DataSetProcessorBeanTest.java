package org.ctoolkit.agent.dataset.processor.impl;

import com.google.guiceberry.junit4.GuiceBerryRule;
import com.googlecode.objectify.ObjectifyService;
import com.googlecode.objectify.util.Closeable;
import org.ctoolkit.agent.UseCaseEnvironment;
import org.ctoolkit.agent.dataset.processor.DataSetProcessor;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import javax.inject.Inject;

/**
 * @author <a href="mailto:aurel.medvegy@ctoolkit.org">Aurel Medvegy</a>
 */
public class DataSetProcessorBeanTest
        extends UseCaseEnvironment
{
    @Rule
    public final GuiceBerryRule guiceBerry = new GuiceBerryRule( UseCaseEnvironment.class );

    // objectify helper
    private Closeable session;

    @Inject
    private DataSetProcessor processor;

    @Test
    public void upgrade() throws Exception
    {
        processor.upgrade( 0L, 10L );

        await( 2 );
    }

    @Before
    public void setUp()
    {
        session = ObjectifyService.begin();
    }

    @After
    public void tearDown()
    {
        session.close();
    }

}