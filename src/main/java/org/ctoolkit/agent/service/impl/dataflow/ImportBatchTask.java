package org.ctoolkit.agent.service.impl.dataflow;

import com.google.cloud.dataflow.sdk.Pipeline;
import com.google.cloud.dataflow.sdk.options.PipelineOptions;
import com.google.cloud.dataflow.sdk.options.PipelineOptionsFactory;
import com.google.cloud.dataflow.sdk.transforms.Create;
import com.google.cloud.dataflow.sdk.transforms.DoFn;
import com.google.cloud.dataflow.sdk.transforms.ParDo;

import java.io.Serializable;

/**
 * @author <a href="mailto:pohorelec@comvai.com">Jozef Pohorelec</a>
 */
public class ImportBatchTask
        implements Serializable
{
    /*
    @Inject
    private transient ImportBatchJob job;

    @Inject
    private static Injector injector;



    private final DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();

*/

    private Long parentId;

    public ImportBatchTask()
    {
    }

    public ImportBatchTask( Long parentId )
    {
        this.parentId = parentId;
    }


    public void run()
    {
//        injector.injectMembers( this );

        PipelineOptions options = PipelineOptionsFactory.create();
//        options.setRunner( DataflowRunner.class ); // TODO: set only for test/prod environment
        Pipeline pipeline = Pipeline.create( options );

        pipeline
                .apply( "Load entities", Create.of( "John", "Foo" ) )
                .apply( "Process entity", ParDo.of( new DoFn<String, Void>()
                {
                    @Override
                    public void processElement( ProcessContext c ) throws Exception
                    {
                        String entity = c.element();
                        System.out.println( ">>> " + entity );
                    }
                } ) );

        pipeline.run();
    }

    /*
    private List<Entity> entities()
    {
        try
        {
            Entity parent = datastore.get( KeyFactory.stringToKey( parentId ) );
            List<Key> itemsRef = ( List<Key> ) parent.getProperty( "itemsRef" );

            return new ArrayList<>( datastore.get( itemsRef ).values() );
        }
        catch ( EntityNotFoundException e )
        {
            throw new IllegalArgumentException( "Unable to load parent key: " + parentId, e );
        }
    }
    */
}
