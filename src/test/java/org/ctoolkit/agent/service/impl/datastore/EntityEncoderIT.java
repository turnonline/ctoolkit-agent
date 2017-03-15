package org.ctoolkit.agent.service.impl.datastore;

import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;
import com.google.guiceberry.junit4.GuiceBerryRule;
import org.ctoolkit.agent.UseCaseEnvironment;
import org.junit.Rule;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * @author <a href="mailto:pohorelec@comvai.com">Jozef Pohorelec</a>
 */
public class EntityEncoderIT
        extends UseCaseEnvironment
{
    @Rule
    public final GuiceBerryRule guiceBerry = new GuiceBerryRule( UseCaseEnvironment.class );

    private EntityEncoder encoder = new EntityEncoder();

    // -- format parent key

    @Test
    public void testFormatKeySimple() throws Exception
    {
        Key key = KeyFactory.createKey( "Foo", 1 );
        assertEquals( "Foo:1", encoder.formatKey( key ) );
    }

    @Test
    public void testFormatKeyOneParent() throws Exception
    {
        Key parent = KeyFactory.createKey( "Bar", 2 );
        Key key = KeyFactory.createKey( parent, "Foo", 1 );
        assertEquals( "Bar:2::Foo:1", encoder.formatKey( key ) );
    }

    @Test
    public void testFormatKeyMultipleParents() throws Exception
    {
        Key superParent = KeyFactory.createKey( "Ipsum", 3 );
        Key parent = KeyFactory.createKey( superParent, "Bar", 2 );
        Key key = KeyFactory.createKey( parent, "Foo", 1 );
        assertEquals( "Ipsum:3::Bar:2::Foo:1", encoder.formatKey( key ) );
    }
}