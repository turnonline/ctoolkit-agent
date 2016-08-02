package org.ctoolkit.migration;

import org.junit.extensions.cpsuite.ClasspathSuite;
import org.junit.extensions.cpsuite.ClasspathSuite.ClassnameFilters;
import org.junit.runner.RunWith;

/**
 * The test suite to run all 'cheap' unit test inside of the project. Based on the
 * naming convention class name ending with 'Test'.
 *
 * @author <a href="mailto:aurel.medvegy@ctoolkit.org">Aurel Medvegy</a>
 */
@RunWith( ClasspathSuite.class )
@ClassnameFilters( {".*Test"} )
public class UnitTestSuite
{

}
