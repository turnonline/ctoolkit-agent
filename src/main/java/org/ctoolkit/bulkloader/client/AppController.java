package org.ctoolkit.bulkloader.client;

import com.google.gwt.activity.shared.Activity;
import com.google.gwt.activity.shared.ActivityMapper;
import com.google.gwt.place.shared.Place;
import org.ctoolkit.bulkloader.client.place.MainPlace;
import org.ctoolkit.bulkloader.client.presenter.MainPresenter;

import javax.inject.Inject;

/**
 * @author <a href="mailto:medvegy@comvai.com>Aurel Medvegy</a>"
 */
public class AppController
        implements ActivityMapper
{

    private MainPresenter mainPresenter;

    @Inject
    public AppController( MainPresenter mainPresenter )
    {
        this.mainPresenter = mainPresenter;
    }

    @Override
    public Activity getActivity( final Place place )
    {
        if ( place instanceof MainPlace )
        {
            return mainPresenter;
        }

        return mainPresenter;
    }
}
