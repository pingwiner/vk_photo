package com.vk_photki.utils

import android.content.Context
import android.media.MediaScannerConnection
import android.net.Uri
import android.os.Environment
import android.util.Log
import org.apache.http.util.ByteArrayBuffer
import java.io.*

/**
 * Created by nightrain on 4/8/15.
 */

public class FsUtils {
    private val TAG ="FsUtils";
    private val GALLERY_PATH = "/VK_gallery/";
    private val BUFFER_SIZE = 4096;

    public fun createExternalStoragePublicPicture(context: Context, inputStream: InputStream, filename: String, albumName:String ) {
        try {
            var path = Environment.getExternalStoragePublicDirectory(
                    Environment.DIRECTORY_PICTURES + GALLERY_PATH);
            path.mkdir();
            path = Environment.getExternalStoragePublicDirectory(
                    Environment.DIRECTORY_PICTURES + GALLERY_PATH + albumName);
            path.mkdir();
            val file = File(path, filename);
            val bis = BufferedInputStream(inputStream);
            var readBytesCount: Int;
            var data = ByteArray(BUFFER_SIZE);
            var fos = FileOutputStream(file);
            var bos = BufferedOutputStream(fos)
            do {
                readBytesCount = bis.read(data)
                if (readBytesCount > 0) {
                    bos.write(data, 0, readBytesCount)
                }
                bos.flush()
            } while(readBytesCount == BUFFER_SIZE)
            bos.close()
            inputStream.close();
            MediaScanner().scanMedia(context, file);
        } catch (e: IOException) {
            Log.w(TAG, "Error writing ", e);
        }
    }
}