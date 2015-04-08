package com.vk_photki.service

import android.content.Context
import android.os.AsyncTask
import com.vk_photki.utils.FsUtils
import java.net.URL

/**
 * Created by nightrain on 4/7/15.
 */

public class WorkerTask(var context: Context, var listener: WorkerTask.OnTaskCompleteListener) : AsyncTask<String, Int, Boolean>() {

    override fun doInBackground(vararg params: String?): Boolean? {
        val albumName = params[1];
        if (albumName == null) return false;
        val urlString = params[0];
        if (urlString == null) return false;
        val url = URL(urlString);
        val fileName = urlString.substring(urlString.lastIndexOf('/') + 1, urlString.length());
        val conn = url.openConnection();
        this.isCancelled()
        FsUtils().createExternalStoragePublicPicture(context, conn.getInputStream(), fileName, albumName)
        return true;
    }

    override fun onPostExecute(result: Boolean) {
        listener.onTaskComplete(result)
    }

    public trait OnTaskCompleteListener {
        public fun onTaskComplete(result: Boolean)
    }
}