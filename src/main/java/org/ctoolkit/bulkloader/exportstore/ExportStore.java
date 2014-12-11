package org.ctoolkit.bulkloader.exportstore;

import org.ctoolkit.bulkloader.changesets.model.ChangeSet;

/**
 * @author <a href="mailto:medvegy@comvai.com">Aurel Medvegy</a>
 */
public interface ExportStore
{

    public final static Long EXPORT_STATUS_EXPORTING = ( long ) 1;
    public final static Long EXPORT_STATUS_DONE = ( long ) 0;

    public final static Long EXPORT_TYPE_FULL_EXPORT = ( long ) 1;
    public final static Long EXPORT_TYPE_DIFF_EXPORT = ( long ) 2;

    /**
     * Method called before the first saveChangeSet call. DataStore can be
     * exported in multiple passes, it always begins with calling the
     * beginExport method. After the last saveChangeSet is called, then
     * finishExport method is called.
     *
     * @param version
     * @return sub version
     */
    public Long beginExport( Long version );

    /**
     * @param version
     * @param subVersion
     */
    public boolean continueExport( Long version, Long subVersion );

    /**
     * Method stores change set in the blob store
     *
     * @param changeSet change set to store
     * @return
     */
    public boolean saveChangeSet( final ChangeSet changeSet );

    /**
     * Method called after the last saveChangeSet call.
     */
    public void finishExport( Long version, Long subVersion );

    /**
     * Method returns the next ChangeSet. It can be called more than once every
     * time you call the method you get the next change set. If there isn't any
     * other, the return value is null
     *
     * @return ChangeSet or null
     */
    public ChangeSet getNextChangeSet();

    /**
     * Method resets the internal pointer to the first full exported change set
     * with version less or equal with upToVersion
     *
     * @param maxVersion
     */
    public void setFirstChangeSet( Long maxVersion );

    /**
     * Method sets the internal pointer to the cursor in the export store.
     * Alternative method for <code>setFirstChangeSet(Long version)</code>
     *
     * @param cursor
     */
    public void setFirstChangeSet( Long maxVersion, Long version, Long subversion, String cursor );

    /**
     * Method return the current cursor position from export store
     *
     * @return cursor in export store
     */
    public String getCursor();

    /**
     * Method returns basic information about the Export store
     *
     * @return export store information
     */
    public String getInfo();
}
