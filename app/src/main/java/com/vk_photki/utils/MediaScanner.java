package com.vk_photki.utils;

import android.content.Context;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.util.Log;

import java.io.File;

/**
 * Created by nightrain on 4/8/15.
 */

public class MediaScanner implements MediaScannerConnection.OnScanCompletedListener {
    private static final String TAG = "MediaScanner";

    public void scanMedia(Context context, File file) {
        MediaScannerConnection.scanFile(context,
            new String[] {file.getAbsolutePath()},
            null,
            this
        );
    }

    public void onScanCompleted(String path, Uri uri) {
        Log.d(TAG, "Scanned " + path + ":");
        Log.d(TAG, "-> uri=" + uri);
    }

}
