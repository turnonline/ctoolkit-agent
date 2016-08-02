package org.ctoolkit.migration.service.impl.datastore;

import com.google.appengine.tools.mapreduce.MapSpecification;

/**
 * Map specification provider
 *
 * @author <a href="mailto:pohorelec@comvai.com">Jozef Pohorelec</a>
 */
public interface MapSpecificationProvider
{
    <T extends MapSpecification> T get();
}
