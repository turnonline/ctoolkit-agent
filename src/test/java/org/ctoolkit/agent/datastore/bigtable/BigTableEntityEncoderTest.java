package org.ctoolkit.agent.datastore.bigtable;

import com.comvai.test.gae.HighReplDatastoreServiceEnvironment;
import com.google.guiceberry.junit4.GuiceBerryRule;
import org.ctoolkit.agent.AgentModule;
import org.ctoolkit.agent.dataset.ChangeSetEntityProperty;
import org.junit.Rule;
import org.junit.Test;

import java.util.Date;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * The AppEngine's datastore (BigTable) entity encoder test.
 *
 * @author <a href="mailto:aurel.medvegy@ctoolkit.org">Aurel Medvegy</a>
 */
public class BigTableEntityEncoderTest
        extends HighReplDatastoreServiceEnvironment
{
    @Rule
    public final GuiceBerryRule guiceBerry = new GuiceBerryRule( BigTableEntityEncoderTest.class );

    @Test
    public void testDecodeProperty() throws Exception
    {
        BigTableEntityEncoder encoder = new BigTableEntityEncoder();

        String input = "xy zaq";
        Object r = encoder.decodeProperty( ChangeSetEntityProperty.PROPERTY_TYPE_STRING, input );
        assertEquals( input, r );

        input = "2341.678";
        r = encoder.decodeProperty( ChangeSetEntityProperty.PROPERTY_TYPE_DOUBLE, input );
        Double aDouble = ( Double ) r;
        assertEquals( 2341.678, aDouble, 0 );

        input = "1765.33";
        r = encoder.decodeProperty( ChangeSetEntityProperty.PROPERTY_TYPE_FLOAT, input );
        Float aFloat = ( Float ) r;
        assertEquals( 1765.33f, aFloat, 0 );

        input = "1765";
        r = encoder.decodeProperty( ChangeSetEntityProperty.PROPERTY_TYPE_INTEGER, input );
        Integer integer = ( Integer ) r;
        assertEquals( new Integer( 1765 ), integer );

        input = "true";
        r = encoder.decodeProperty( ChangeSetEntityProperty.PROPERTY_TYPE_BOOLEAN, input );
        Boolean bool = ( Boolean ) r;
        assertTrue( bool );

        input = "false";
        r = encoder.decodeProperty( ChangeSetEntityProperty.PROPERTY_TYPE_BOOLEAN, input );
        bool = ( Boolean ) r;
        assertFalse( bool );

        input = "1265110426299";
        r = encoder.decodeProperty( ChangeSetEntityProperty.PROPERTY_TYPE_DATE, input );
        assertEquals( new Date( 1265110426299L ), r );

        input = "Account:2";
        r = encoder.decodeProperty( ChangeSetEntityProperty.PROPERTY_TYPE_KEY, input );
        assertEquals( "Account(2)", r.toString() );

        input = "Account:2::User:3::Address:5";
        r = encoder.decodeProperty( ChangeSetEntityProperty.PROPERTY_TYPE_KEY, input );
        assertEquals( "Account(2)/User(3)/Address(5)", r.toString() );
    }

    @Override
    public void configureTestBinder()
    {
        install( new AgentModule() );
    }
}
