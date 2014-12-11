package org.ctoolkit.bulkloader;

import org.junit.extensions.cpsuite.ClasspathSuite;
import org.junit.extensions.cpsuite.ClasspathSuite.ClassnameFilters;
import org.junit.runner.RunWith;

/**
 * The integration test and use case suite to run all 'expensive' test inside of the project. Based on the
 * naming convention class name ending with 'IT' and 'UseCase'.
 *
 * @author <a href="mailto:medvegy@comvai.com">Aurel Medvegy</a>
 */
@RunWith( ClasspathSuite.class )
@ClassnameFilters( {".*IT", ".*UseCase"} )
public class ITSuite
{

}
