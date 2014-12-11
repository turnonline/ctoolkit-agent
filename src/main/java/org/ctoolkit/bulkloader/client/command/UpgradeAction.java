package org.ctoolkit.bulkloader.client.command;

import com.comvai.gwt.common.command.Command;
import com.comvai.gwt.common.command.IAction;
import org.ctoolkit.bulkloader.facade.JobFacade;

/**
 * @author <a href="mailto:medvegy@comvai.com">Aurel Medvegy</a>
 */
@Command( methodName = "upgrade", facadeClass = JobFacade.class )
public class UpgradeAction
        extends IAction<UpgradeResponse>
{
    public UpgradeAction()
    {
    }
}

