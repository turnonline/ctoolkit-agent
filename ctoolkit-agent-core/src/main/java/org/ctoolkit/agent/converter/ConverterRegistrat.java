package org.ctoolkit.agent.converter;

/**
 * Converter registrat API
 *
 * @author <a href="mailto:pohorelec@turnonlie.biz">Jozef Pohorelec</a>
 */
public interface ConverterRegistrat
{
    /**
     * Register converter
     *
     * @param source source class
     * @param target target value
     * @param converter converter to use
     */
    void register( Class source, String target, Converter converter );

    /**
     * Get converter by source value ant target type name
     *
     * @param sourceValue source value
     * @param targetTypeName target type name (i.e. text)
     * @return converter or <code>null</code> if converter could not be found for sourceValue/targetTypeName combination
     */
    Converter get( Object sourceValue, String targetTypeName );
}
