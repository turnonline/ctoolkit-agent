package org.ctoolkit.bulkloader.client.place;

import com.google.gwt.place.shared.Place;
import com.google.gwt.place.shared.PlaceTokenizer;

/**
 * @author <a href="mailto:medvegy@comvai.com>Aurel Medvegy</a>"
 */
public class MainPlace
        extends Place
{
    public static class Tokenizer
            implements PlaceTokenizer<MainPlace>
    {

        @Override
        public String getToken( MainPlace place )
        {
            return "";
        }

        @Override
        public MainPlace getPlace( String token )
        {
            return new MainPlace();
        }
    }
}
