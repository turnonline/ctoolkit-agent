package org.ctoolkit.migration.agent.service.impl.datastore;

import com.google.appengine.tools.mapreduce.MapSpecification;

/**
 * Map specification provider
 *
 * @author <a href="mailto:jozef.pohorelec@ctoolkit.org">Jozef Pohorelec</a>
 */
public interface MapSpecificationProvider
{
    <T extends MapSpecification> T get();
}
