package org.ctoolkit.bulkloader.client.place;

import com.google.gwt.place.shared.PlaceHistoryMapper;
import com.google.gwt.place.shared.WithTokenizers;

/**
 * @author <a href="mailto:medvegy@comvai.com>Aurel Medvegy</a>"
 */
@WithTokenizers( {MainPlace.Tokenizer.class} )
public interface BulkLoaderPlaceHistoryMapper
        extends PlaceHistoryMapper
{
}
