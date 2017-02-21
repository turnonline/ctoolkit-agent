package org.ctoolkit.migration.agent.util;

import com.google.api.client.util.Charsets;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.transform.stream.StreamSource;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * XML utility is used to marshall/unmarshall of XMLs
 *
 * @author <a href="mailto:jozef.pohorelec@ctoolkit.org">Jozef Pohorelec</a>
 */
public class XmlUtils
{
    private XmlUtils()
    {
    }

    /**
     * Unmarshall input source into java object
     *
     * @param source                     contains xml source
     * @param classToBeBound             class type of returning object
     * @param additionalClassesToBeBound additional classes to bound into jaxb context, i.e. ObjectFactory
     * @param <T>                        java object result type
     * @return unmarshaled java object
     */
    @SuppressWarnings( "unchecked" )
    public static <T> T unmarshall( InputStream source, Class<T> classToBeBound, Class... additionalClassesToBeBound )
    {
        try
        {
            List<Class> classes = new ArrayList<>( Arrays.asList( additionalClassesToBeBound ) );
            classes.add( classToBeBound );

            JAXBContext jaxbContext = JAXBContext.newInstance( classes.toArray( new Class[classes.size()] ) );
            Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
            JAXBElement<T> jaxbElement = unmarshaller.unmarshal( new StreamSource( source ), classToBeBound );
            return jaxbElement.getValue();
        }
        catch ( JAXBException e )
        {
            throw new RuntimeException( "Error occur during unmarshalling class: " + classToBeBound, e );
        }
    }

    /**
     * Marshal object into string
     *
     * @param source                     java object
     * @param additionalClassesToBeBound additional classes to bound into jaxb context, i.e. ObjectFactory
     * @param <T>                        java object type
     * @return xml marshaled from java source
     */
    public static <T> String marshall( T source, Class... additionalClassesToBeBound )
    {
        try
        {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();

            List<Class> classes = new ArrayList<>( Arrays.asList( additionalClassesToBeBound ) );
            classes.add( source.getClass() );

            JAXBContext jaxbContext = JAXBContext.newInstance( classes.toArray( new Class[classes.size()] ) );
            Marshaller marshaller = jaxbContext.createMarshaller();
            marshaller.marshal( source, baos );

            return baos.toString( Charsets.UTF_8.name() );
        }
        catch ( JAXBException | UnsupportedEncodingException e )
        {
            throw new RuntimeException( "Error occur during marshalling object: " + source, e );
        }
    }
}
