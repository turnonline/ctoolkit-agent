package org.ctoolkit.agent.transformer;

import org.apache.commons.text.StringSubstitutor;
import org.ctoolkit.agent.model.api.MigrationSet;
import org.ctoolkit.agent.model.api.MigrationSetPropertyPatternTransformer;

import java.util.HashMap;
import java.util.Map;

/**
 * Implementation of {@link MigrationSetPropertyPatternTransformer} transformer
 *
 * @author <a href="mailto:pohorelec@turnonlie.biz">Jozef Pohorelec</a>
 */
public class PatternTransformerProcessor
        implements TransformerProcessor<MigrationSetPropertyPatternTransformer>
{
    @Override
    public Object transform( Object value, MigrationSetPropertyPatternTransformer transformer, Map<Object, Object> ctx )
    {
        if ( value instanceof String )
        {
            MigrationSet migrationSet = (MigrationSet) ctx.get( MigrationSet.class );

            Map<String, String> placeholders = new HashMap<>();
            placeholders.put( "source.namespace", migrationSet.getSource().getNamespace() );
            placeholders.put( "source.kind", migrationSet.getSource().getKind() );

            placeholders.put( "target.namespace", migrationSet.getTarget().getNamespace() );
            placeholders.put( "target.kind", migrationSet.getTarget().getKind() );
            placeholders.put( "target.value", ( String ) value );

            StringSubstitutor substitution = new StringSubstitutor( placeholders, "{", "}" );
            value = substitution.replace( transformer.getPattern() );
        }

        return value;
    }
}
