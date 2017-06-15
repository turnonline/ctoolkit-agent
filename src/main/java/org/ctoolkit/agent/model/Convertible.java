package org.ctoolkit.agent.model;

import com.google.cloud.datastore.Entity;

/**
 * @author <a href="mailto:pohorelec@comvai.com">Jozef Pohorelec</a>
 */
public interface Convertible
{
    void convert( Entity entity );
}
