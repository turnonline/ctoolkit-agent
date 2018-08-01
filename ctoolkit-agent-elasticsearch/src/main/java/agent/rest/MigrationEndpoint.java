package agent.rest;

import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Post;
import io.reactivex.Single;
import org.ctoolkit.agent.model.api.ImportBatch;
import org.ctoolkit.agent.model.api.ImportJob;
import org.ctoolkit.agent.model.api.MigrationBatch;
import org.ctoolkit.agent.model.api.MigrationJob;
import org.ctoolkit.agent.service.MigrationService;

import javax.inject.Inject;

/**
 * Migration public REST api
 *
 * @author <a href="mailto:pohorelec@turnonline.biz">Jozef Pohorelec</a>
 */
@Controller( "/api/v1" )
public class MigrationEndpoint
{
    @Inject
    private MigrationService service;

    @Post( "/migrations" )
    public Single<MigrationJob> migrateBatch( MigrationBatch batch )
    {
        return Single.just( service.migrateBatch( batch ) );
    }

    @Post( "/imports" )
    public Single<ImportJob> importBatch( ImportBatch batch )
    {
        return Single.just( service.importBatch( batch ) );
    }
}
