package org.ctoolkit.agent.converter;

import org.ctoolkit.agent.model.api.MigrationSetProperty;

import java.util.Objects;

/**
 * API for converters
 *
 * @author <a href="mailto:pohorelec@turnonlie.biz">Jozef Pohorelec</a>
 */
public interface Converter
{
    /**
     * Convert source value to target value using {@link MigrationSetProperty}
     *
     * @param source   source exported value
     * @param property {@link MigrationSetProperty}
     * @return converted string value
     */
    String convert( Object source, MigrationSetProperty property );

    /**
     * Provide converter key used in {@link BaseConverterRegistrat#register(Class, String, Converter)} method
     *
     * @param source source exported value
     * @param target target property string value, i.e. 'text'
     * @return {@link Key}
     */
    default Key key( Class source, String target )
    {
        return new Key( source, target );
    }

    /**
     * Converter key used in {@link BaseConverterRegistrat#register(Class, String, Converter)}
     */
    class Key
    {
        private Class sourceClassName;

        private String targetTypeName;

        public Key( Class sourceClassName, String targetTypeName )
        {
            this.sourceClassName = sourceClassName;
            this.targetTypeName = targetTypeName;
        }

        public Class getSourceClassName()
        {
            return sourceClassName;
        }

        public String getTargetTypeName()
        {
            return targetTypeName;
        }

        @Override
        public boolean equals( Object o )
        {
            if ( this == o ) return true;
            if ( !( o instanceof Key ) ) return false;
            Key key = ( Key ) o;
            return Objects.equals( sourceClassName, key.sourceClassName.asSubclass( sourceClassName )) &&
                    Objects.equals( targetTypeName, key.targetTypeName );
        }

        @Override
        public int hashCode()
        {
            return Objects.hash( sourceClassName, targetTypeName );
        }

        @Override
        public String toString()
        {
            return "ConverterKey{" +
                    "sourceClassName='" + sourceClassName + '\'' +
                    ", targetTypeName='" + targetTypeName + '\'' +
                    '}';
        }
    }

}
