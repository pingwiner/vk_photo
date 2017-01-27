package com.vk_photki.service

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Matrix
import android.net.Uri
import android.os.AsyncTask
import android.util.Log
import com.vk_photki.utils.FsUtils
import com.vk_photki.utils.getCacheFileFor
import java.net.URL

/**
 * Created by nightrain on 4/12/15.
 */

public class ImgCacheTask(var context: Context, var listener: ImgCacheTask.OnTaskCompleteListener) :
        AsyncTask<String, Int, Boolean>() {
    private val TAG = "ImgCacheTask";
    private var urlString: String? = null;

    override fun doInBackground(vararg params: String?): Boolean? {
        urlString = params[0];
        if (urlString == null) return false;
        Log.d(TAG, "loading " + urlString);
        val url = URL(urlString);
        val conn = url.openConnection();
        FsUtils().createCachePicture(context, conn.getInputStream(), urlString!!)
        return true;
    }

    override fun onPostExecute(result: Boolean) {
        if (result) {
            listener.onTaskComplete(result, Uri.fromFile(getCacheFileFor(context, urlString!!)))
        } else {
            listener.onTaskComplete(result, null)
        }

    }

    public interface OnTaskCompleteListener {
        public fun onTaskComplete(result: Boolean, url: Uri?)
    }

    private fun scaleImage(bitmap: Bitmap, boundBoxInDp: Int): Bitmap {
        // Get current dimensions
        val width = bitmap.getWidth();
        val height = bitmap.getHeight();

        // Determine how much to scale: the dimension requiring less scaling is
        // closer to the its side. This way the image always stays inside your
        // bounding box AND either x/y axis touches it.
        val xScale = boundBoxInDp / width;
        val yScale = boundBoxInDp / height;
        var scale: Float = 0f;
        if (xScale <= yScale) {
            scale = xScale.toFloat()
        } else {
            scale = yScale.toFloat()
        }

        // Create a matrix for the scaling and add the scaling data
        val matrix = Matrix();
        matrix.postScale(scale, scale);

        // Create a new bitmap and convert it to a format understood by the ImageView
        return Bitmap.createBitmap(bitmap, 0, 0, width, height, matrix, true);
    }

    private fun dpToPx(dp: Int): Int {
        val density = context.getApplicationContext().getResources().getDisplayMetrics().density;
        return Math.round(dp * density);
    }
}