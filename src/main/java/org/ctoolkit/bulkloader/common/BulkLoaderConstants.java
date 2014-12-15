/**
 *
 */
package org.ctoolkit.bulkloader.common;

/**
 * @author <a href="mailto:medvegy@comvai.com">Aurel Medvegy</a>
 */
public final class BulkLoaderConstants
{

    /**
     * Input parameter
     */
    public static final String COMMAND_MODE = "mode";

    public static final String COMMAND_MODE_IMPORT = "import";

    public static final String COMMAND_MODE_EXPORT = "export";

    public static final String COMMAND_MODE_UPGRADE = "upgrade";

    public static final String COMMAND_MODE_INFO = "info";

    public static final String PARAM_EXPORT_JOB = "export-job";

    public static final String PARAM_VERSION = "version";

    public static final Long BULKLOADER_RESULT_OK = ( long ) 0;

    public static final Long BULKLOADER_RESULT_ERROR = ( long ) -1;

    /**
     * predefined request parameter keys
     */
    public final static String PROP_VERSION = "__blprop_version";

    public final static String PROP_SUBVERSION = "__blprop_subversion";

    public final static String PROP_MAXVERSION = "__blprop_maxversion";

    public final static String PROP_CURSOR = "__blprop_cursor";

    public final static String PROP_STATE = "__blprop_state";

    public final static String PROP_EXPORT_JOB = "__blprop_expjob";

    public final static String PROP_EXPORT_KIND = "__blprop_expkind";

    public final static String NEWLINE = "<br/>";
}
