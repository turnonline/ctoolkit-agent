package org.ctoolkit.agent.service.impl.dataflow.migration;

import org.ctoolkit.agent.exception.MigrationUseCaseNotExists;
import org.ctoolkit.agent.resource.MigrationSetKindOperation;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;

/**
 * Use case resolver
 *
 * @author <a href="mailto:pohorelec@comvai.com">Jozef Pohorelec</a>
 */
public class UseCaseResolver
{
    private List<UseCase> useCases = new ArrayList<>();

    @Inject
    public UseCaseResolver( Add__NewName_NewType_NewValue add__NewName_NewType_NewValue,
                            Add__NewName_NewType_NewValueNull add__NewName_NewType_NewValueNull,

                            Remove__Kind remove__Kind,
                            Remove__Property remove__Property,

                            Change__NewName change__NewName,
                            Change__NewType change__NewType,
                            Change__NewValue change__NewValue,
                            Change__NewName_NewType change__NewName_newType,
                            Change__NewName_NewValue change__NewName_NewValue,
                            Change__NewType_NewValue change__NewType_NewValue,
                            Change__NewName_NewType_NewValue change__NewName_NewType_NewValue )
    {
        // ADD
        useCases.add( add__NewName_NewType_NewValue );
        useCases.add( add__NewName_NewType_NewValueNull );

        // REMOVE
        useCases.add( remove__Kind );
        useCases.add( remove__Property );

        // CHANGE
        useCases.add( change__NewName );
        useCases.add( change__NewType );
        useCases.add( change__NewValue );
        useCases.add( change__NewName_newType );
        useCases.add( change__NewName_NewValue );
        useCases.add( change__NewType_NewValue );
        useCases.add( change__NewName_NewType_NewValue );
    }

    public UseCase resolve( MigrationSetKindOperation operation )
    {
        for ( UseCase useCase : useCases )
        {
            if ( useCase.apply( operation ) )
            {
                return useCase;
            }
        }

        throw new MigrationUseCaseNotExists( "Use case does not exists or is not implemented yet for operation: " + operation );
    }
}
