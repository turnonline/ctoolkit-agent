package org.ctoolkit.agent.service.impl.dataflow.migration;

import com.google.cloud.datastore.Entity;
import com.google.cloud.datastore.Key;
import com.google.cloud.datastore.NullValue;
import com.google.cloud.datastore.StringValue;
import com.google.cloud.datastore.Value;
import org.ctoolkit.agent.exception.MigrationUseCaseNotExists;
import org.ctoolkit.agent.resource.MigrationSetKindOperation;
import org.ctoolkit.agent.resource.MigrationSetKindOperationAdd;
import org.ctoolkit.agent.resource.MigrationSetKindOperationChange;
import org.ctoolkit.agent.resource.MigrationSetKindOperationRemove;
import org.ctoolkit.agent.service.impl.datastore.EntityDecoder;
import org.ctoolkit.agent.service.impl.datastore.EntityEncoder;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

/**
 * Unit test for UseCaseResolver
 *
 * @author <a href="mailto:pohorelec@comvai.com">Jozef Pohorelec</a>
 */
public class UseCaseResolverTest
{
    private EntityEncoder encoder = new EntityEncoder();
    private EntityDecoder decoder = new EntityDecoder();
    private IRuleStrategyResolver strategyResolver;

    private Add__NewName_NewType_NewValue add__NewName_NewType_NewValue = new Add__NewName_NewType_NewValue( strategyResolver, decoder );
    private Add__NewName_NewType_NewValueNull add__NewName_NewType_NewValueNull = new Add__NewName_NewType_NewValueNull( strategyResolver );
    private Remove__Kind remove__Kind = new Remove__Kind( strategyResolver );
    private Remove__Property remove__Property = new Remove__Property( strategyResolver );
    private Change__NewName change__NewName = new Change__NewName( strategyResolver );
    private Change__NewType change__NewName_NewType = new Change__NewType( strategyResolver, encoder, decoder );
    private Change__NewValue change__NewValue = new Change__NewValue( strategyResolver, encoder, decoder );
    private Change__NewName_NewType change__NewName_newType = new Change__NewName_NewType( strategyResolver, encoder, decoder );
    private Change__NewName_NewValue change__NewName_NewValue = new Change__NewName_NewValue( strategyResolver, encoder, decoder );
    private Change__NewType_NewValue change__NewType_NewValue = new Change__NewType_NewValue( strategyResolver, decoder );
    private Change__NewName_NewType_NewValue change__NewName_NewType_NewValue = new Change__NewName_NewType_NewValue( strategyResolver, decoder );

    private UseCaseResolver resolver;

    @Before
    public void setUp() throws Exception
    {
        resolver = new UseCaseResolver(
                add__NewName_NewType_NewValue,
                add__NewName_NewType_NewValueNull,

                remove__Kind,
                remove__Property,

                change__NewName,
                change__NewName_NewType,
                change__NewValue,
                change__NewName_newType,
                change__NewName_NewValue,
                change__NewType_NewValue,
                change__NewName_NewType_NewValue
        );
    }

    // -- add

    @Test( expected = MigrationUseCaseNotExists.class )
    public void test_Add__NewKind()
    {
        MigrationSetKindOperation operation = new MigrationSetKindOperationAdd();
        operation.setNewKind( "_Person" );

        resolver.resolve( operation );
    }

    @Test( expected = MigrationUseCaseNotExists.class )
    public void test_Add__NewName()
    {
        MigrationSetKindOperation operation = new MigrationSetKindOperationAdd();
        operation.setNewName( "_name" );

        resolver.resolve( operation );
    }

    @Test( expected = MigrationUseCaseNotExists.class )
    public void test_Add__NewType()
    {
        MigrationSetKindOperation operation = new MigrationSetKindOperationAdd();
        operation.setNewType( "string" );

        resolver.resolve( operation );
    }

    @Test( expected = MigrationUseCaseNotExists.class )
    public void test_Add__NewValue()
    {
        MigrationSetKindOperation operation = new MigrationSetKindOperationAdd();
        operation.setNewValue( "Mike" );

        resolver.resolve( operation );
    }

    @Test( expected = MigrationUseCaseNotExists.class )
    public void test_Add__NewKind_NewName()
    {
        MigrationSetKindOperation operation = new MigrationSetKindOperationAdd();
        operation.setNewKind( "_Person" );
        operation.setNewName( "_name" );

        resolver.resolve( operation );
    }

    @Test( expected = MigrationUseCaseNotExists.class )
    public void test_Add__NewKind_NewType()
    {
        MigrationSetKindOperation operation = new MigrationSetKindOperationAdd();
        operation.setNewKind( "_Person" );
        operation.setNewType( "string" );

        resolver.resolve( operation );
    }

    @Test( expected = MigrationUseCaseNotExists.class )
    public void test_Add__NewKind_NewValue()
    {
        MigrationSetKindOperation operation = new MigrationSetKindOperationAdd();
        operation.setNewKind( "_Person" );
        operation.setNewValue( "Mike" );

        resolver.resolve( operation );
    }

    @Test
    public void test_Add__NewName_NewType_NewValueNull()
    {
        MigrationSetKindOperation operation = new MigrationSetKindOperationAdd();
        operation.setKind( "Person" );
        operation.setNewType( "string" );
        operation.setNewName( "_name" );

        UseCase useCase = resolver.resolve( operation );

        assertTrue( useCase instanceof Add__NewName_NewType_NewValueNull );
        assertEquals( "_name", useCase.name( operation ) );
        assertTrue( useCase.value( operation, null ) instanceof NullValue );
        assertFalse( useCase.removeOldProperty() );
        assertFalse( useCase.removeEntity() );
    }

    @Test( expected = MigrationUseCaseNotExists.class )
    public void test_Add__NewName_NewValue()
    {
        MigrationSetKindOperation operation = new MigrationSetKindOperationAdd();
        operation.setNewName( "_name" );
        operation.setNewValue( "Mike" );

        resolver.resolve( operation );
    }

    @Test( expected = MigrationUseCaseNotExists.class )
    public void test_Add__NewType_NewValue()
    {
        MigrationSetKindOperation operation = new MigrationSetKindOperationAdd();
        operation.setNewType( "string" );
        operation.setNewValue( "Mike" );

        resolver.resolve( operation );
    }


    @Test( expected = MigrationUseCaseNotExists.class )
    public void test_Add__NewKind_NewName_NewType()
    {
        MigrationSetKindOperation operation = new MigrationSetKindOperationAdd();
        operation.setNewKind( "_Person" );
        operation.setNewName( "Mike" );
        operation.setNewType( "string" );

        resolver.resolve( operation );
    }

    @Test( expected = MigrationUseCaseNotExists.class )
    public void test_Add__NewKind_NewType_NewValue()
    {
        MigrationSetKindOperation operation = new MigrationSetKindOperationAdd();
        operation.setNewKind( "_Person" );
        operation.setNewType( "string" );
        operation.setNewValue( "Mike" );

        resolver.resolve( operation );
    }

    @Test
    public void test_Add__NewName_NewType_NewValue()
    {
        Entity entity = createEntityBuilder( "foo", new StringValue( "bar" ) ).build();

        MigrationSetKindOperation operation = new MigrationSetKindOperationAdd();
        operation.setKind( "Person" );
        operation.setNewType( "string" );
        operation.setNewName( "_name" );
        operation.setNewValue( "Mike" );

        UseCase useCase = resolver.resolve( operation );

        assertTrue( useCase instanceof Add__NewName_NewType_NewValue );
        assertEquals( "_name", useCase.name( operation ) );
        assertEquals( "Mike", useCase.value( operation, entity ).get() );
        assertFalse( useCase.removeOldProperty() );
        assertFalse( useCase.removeEntity() );
    }

    @Test( expected = MigrationUseCaseNotExists.class )
    public void test_Add__NewKind_NewName_NewValue()
    {
        MigrationSetKindOperation operation = new MigrationSetKindOperationAdd();
        operation.setNewKind( "_Person" );
        operation.setNewName( "Mike" );
        operation.setNewValue( "Mike" );

        resolver.resolve( operation );
    }


    @Test( expected = MigrationUseCaseNotExists.class )
    public void test_Add__NewKind_NewName_NewType_NewValue()
    {
        MigrationSetKindOperation operation = new MigrationSetKindOperationAdd();
        operation.setNewKind( "_Person" );
        operation.setNewName( "Mike" );
        operation.setNewType( "string" );
        operation.setNewValue( "Mike" );

        resolver.resolve( operation );
    }

    // -- remove

    @Test
    public void test_Remove__Kind()
    {
        MigrationSetKindOperation operation = new MigrationSetKindOperationRemove();
        operation.setKind( "Person" );

        UseCase useCase = resolver.resolve( operation );

        assertTrue( useCase instanceof Remove__Kind );
        assertNull( useCase.name( operation ) );
        assertNull( useCase.value( operation, null ) );
        assertFalse( useCase.removeOldProperty() );
        assertTrue( useCase.removeEntity() );
    }

    @Test
    public void test_Remove__Property()
    {
        MigrationSetKindOperation operation = new MigrationSetKindOperationRemove();
        operation.setKind( "Person" );
        operation.setProperty( "name" );

        UseCase useCase = resolver.resolve( operation );

        assertTrue( useCase instanceof Remove__Property );
        assertNull( useCase.name( operation ) );
        assertNull( useCase.value( operation, null ) );
        assertTrue( useCase.removeOldProperty() );
        assertFalse( useCase.removeEntity() );
    }

    // -- change

    @Test( expected = MigrationUseCaseNotExists.class )
    public void test_Change__NewKind()
    {
        MigrationSetKindOperation operation = new MigrationSetKindOperationChange();
        operation.setNewKind( "_Person" );

        resolver.resolve( operation );
    }

    @Test
    public void test_Change__NewName()
    {
        Entity entity = createEntityBuilder( "name", new StringValue( "John" ) ).build();

        MigrationSetKindOperation operation = new MigrationSetKindOperationChange();
        operation.setKind( "Person" );
        operation.setProperty( "name" );
        operation.setNewName( "_name" );

        UseCase useCase = resolver.resolve( operation );

        assertTrue( useCase instanceof Change__NewName );
        assertEquals( "_name", useCase.name( operation ) );
        assertEquals( "John", useCase.value( operation, entity ).get() );
        assertTrue( useCase.removeOldProperty() );
        assertFalse( useCase.removeEntity() );
    }

    @Test
    public void test_Change__NewType()
    {
        Entity entity = createEntityBuilder( "name", new StringValue( "1" ) ).build();

        MigrationSetKindOperation operation = new MigrationSetKindOperationChange();
        operation.setKind( "Person" );
        operation.setProperty( "name" );
        operation.setNewType( "long" );

        UseCase useCase = resolver.resolve( operation );

        assertTrue( useCase instanceof Change__NewType );
        assertEquals( "name", useCase.name( operation ) );
        assertEquals( 1L, useCase.value( operation, entity ).get() );
        assertTrue( useCase.removeOldProperty() );
        assertFalse( useCase.removeEntity() );
    }

    @Test
    public void test_Change__NewValue()
    {
        Entity entity = createEntityBuilder( "name", new StringValue( "John" ) ).build();

        MigrationSetKindOperation operation = new MigrationSetKindOperationChange();
        operation.setKind( "Person" );
        operation.setProperty( "name" );
        operation.setNewValue( "Mike" );

        UseCase useCase = resolver.resolve( operation );

        assertTrue( useCase instanceof Change__NewValue );
        assertEquals( "name", useCase.name( operation ) );
        assertEquals( "Mike", useCase.value( operation, entity ).get() );
        assertFalse( useCase.removeOldProperty() );
        assertFalse( useCase.removeEntity() );
    }

    @Test( expected = MigrationUseCaseNotExists.class )
    public void test_Change__NewKind_NewName()
    {
        MigrationSetKindOperation operation = new MigrationSetKindOperationChange();
        operation.setNewKind( "_Person" );
        operation.setNewName( "_name" );

        resolver.resolve( operation );
    }

    @Test( expected = MigrationUseCaseNotExists.class )
    public void test_Change__NewKind_NewType()
    {
        MigrationSetKindOperation operation = new MigrationSetKindOperationChange();
        operation.setNewKind( "_Person" );
        operation.setNewType( "string" );

        resolver.resolve( operation );
    }

    @Test( expected = MigrationUseCaseNotExists.class )
    public void test_Change__NewKind_NewValue()
    {
        MigrationSetKindOperation operation = new MigrationSetKindOperationChange();
        operation.setNewKind( "_Person" );
        operation.setNewValue( "Mike" );

        resolver.resolve( operation );
    }

    @Test
    public void test_Change__NewName_NewType()
    {
        Entity entity = createEntityBuilder( "name", new StringValue( "1" ) ).build();

        MigrationSetKindOperation operation = new MigrationSetKindOperationChange();
        operation.setKind( "Person" );
        operation.setProperty( "name" );
        operation.setNewName( "_name" );
        operation.setNewType( "long" );

        UseCase useCase = resolver.resolve( operation );

        assertTrue( useCase instanceof Change__NewName_NewType );
        assertEquals( "_name", useCase.name( operation ) );
        assertEquals( 1L, useCase.value( operation, entity ).get() );
        assertTrue( useCase.removeOldProperty() );
        assertFalse( useCase.removeEntity() );
    }

    @Test
    public void test_Change__NewName_NewValue()
    {
        Entity entity = createEntityBuilder( "name", new StringValue( "John" ) ).build();

        MigrationSetKindOperation operation = new MigrationSetKindOperationChange();
        operation.setKind( "Person" );
        operation.setProperty( "name" );
        operation.setNewName( "_name" );
        operation.setNewValue( "Mike" );

        UseCase useCase = resolver.resolve( operation );

        assertTrue( useCase instanceof Change__NewName_NewValue );
        assertEquals( "_name", useCase.name( operation ) );
        assertEquals( "Mike", useCase.value( operation, entity ).get() );
        assertTrue( useCase.removeOldProperty() );
        assertFalse( useCase.removeEntity() );
    }

    @Test
    public void test_Change__NewType_NewValue()
    {
        Entity entity = createEntityBuilder( "name", new StringValue( "1" ) ).build();

        MigrationSetKindOperation operation = new MigrationSetKindOperationChange();
        operation.setKind( "Person" );
        operation.setProperty( "name" );
        operation.setNewType( "long" );
        operation.setNewValue( "2" );

        UseCase useCase = resolver.resolve( operation );

        assertTrue( useCase instanceof Change__NewType_NewValue );
        assertEquals( "name", useCase.name( operation ) );
        assertEquals( 2L, useCase.value( operation, entity ).get() );
        assertTrue( useCase.removeOldProperty() );
        assertFalse( useCase.removeEntity() );
    }

    @Test( expected = MigrationUseCaseNotExists.class )
    public void test_Change__NewKind_NewName_NewType()
    {
        MigrationSetKindOperation operation = new MigrationSetKindOperationChange();
        operation.setNewKind( "_Person" );
        operation.setNewName( "_name" );
        operation.setNewType( "string" );

        resolver.resolve( operation );
    }

    @Test( expected = MigrationUseCaseNotExists.class )
    public void test_Change__NewKind_NewType_NewValue()
    {
        MigrationSetKindOperation operation = new MigrationSetKindOperationChange();
        operation.setNewKind( "_Person" );
        operation.setNewType( "long" );
        operation.setNewValue( "Mike" );

        resolver.resolve( operation );
    }

    @Test
    public void test_Change__NewName_NewType_NewValue()
    {
        Entity entity = createEntityBuilder( "name", new StringValue( "1" ) ).build();

        MigrationSetKindOperation operation = new MigrationSetKindOperationChange();
        operation.setKind( "Person" );
        operation.setProperty( "name" );
        operation.setNewName( "_name" );
        operation.setNewType( "long" );
        operation.setNewValue( "2" );

        UseCase useCase = resolver.resolve( operation );

        assertTrue( useCase instanceof Change__NewName_NewType_NewValue );
        assertEquals( "_name", useCase.name( operation ) );
        assertEquals( 2L, useCase.value( operation, entity ).get() );
        assertTrue( useCase.removeOldProperty() );
        assertFalse( useCase.removeEntity() );
    }

    @Test( expected = MigrationUseCaseNotExists.class )
    public void test_Change__NewKind_NewName_NewValue()
    {
        MigrationSetKindOperation operation = new MigrationSetKindOperationChange();
        operation.setNewKind( "_Person" );
        operation.setNewName( "_name" );
        operation.setNewValue( "Mike" );

        resolver.resolve( operation );
    }

    @Test( expected = MigrationUseCaseNotExists.class )
    public void test_Change__NewKind_NewName_NewType_NewValue()
    {
        MigrationSetKindOperation operation = new MigrationSetKindOperationChange();
        operation.setNewKind( "_Person" );
        operation.setNewName( "_name" );
        operation.setNewType( "string" );
        operation.setNewValue( "Mike" );

        resolver.resolve( operation );
    }

    // -- private helpers

    private Entity.Builder createEntityBuilder( String name, Value<?> value )
    {
        return Entity
                .newBuilder( Key.newBuilder( "c-toolkit", "Person", 1 ).build() )
                .set( name, value );
    }

}