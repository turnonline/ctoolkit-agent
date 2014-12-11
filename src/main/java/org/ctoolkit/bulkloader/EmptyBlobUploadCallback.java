package org.ctoolkit.bulkloader;

import com.comvai.services.blob.BlobUploadCallback;

/**
 * @author <a href="mailto:medvegy@comvai.com">Aurel Medvegy</a>
 */
public class EmptyBlobUploadCallback
        implements BlobUploadCallback
{
    @Override
    public byte[] onUpload( byte[] data, String blobType )
    {
        return new byte[0];
    }
}
