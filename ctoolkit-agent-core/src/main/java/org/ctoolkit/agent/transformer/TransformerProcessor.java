package org.ctoolkit.agent.transformer;

import org.ctoolkit.agent.model.api.MigrationSetPropertyTransformer;

/**
 * Transformer processor is used to implement {@link MigrationSetPropertyTransformer} transform logic
 *
 * @author <a href="mailto:pohorelec@turnonlie.biz">Jozef Pohorelec</a>
 */
public interface TransformerProcessor<TRANSFORMER extends MigrationSetPropertyTransformer>
{
    Object transform( Object value, TRANSFORMER transformer );
}
