package org.ctoolkit.bulkloader.client.command;

import com.comvai.gwt.common.command.Command;
import com.comvai.gwt.common.command.IAuthAction;
import org.ctoolkit.bulkloader.facade.JobFacade;

/**
 * @author <a href="mailto:medvegy@comvai.com">Aurel Medvegy</a>
 */
@Command( methodName = "get", facadeClass = JobFacade.class )
public class GetAction
        extends IAuthAction<GetResponse>
{
    public GetAction()
    {
    }
}
