package com.vk_photki.service

import android.content.Context
import android.os.AsyncTask
import android.util.Log
import com.vk_photki.utils.FsUtils
import java.net.URL

/**
 * Created by nightrain on 4/7/15.
 */

public class WorkerTask(var context: Context, var listener: WorkerTask.OnTaskCompleteListener) : AsyncTask<String, Int, Boolean>() {
    private val TAG = "WorkerTask";
    private var mFileName: String? = null;
    private var mComplete = false;

    override fun doInBackground(vararg params: String?): Boolean? {
        val albumName = params[1];
        if (albumName == null) return false;
        val urlString = params[0];
        if (urlString == null) return false;
        Log.d(TAG, "loading " + urlString);
        val url = URL(urlString);
        mFileName = urlString.substring(urlString.lastIndexOf('/') + 1, urlString.length());
        val conn = url.openConnection();
        FsUtils().createExternalStoragePublicPicture(context, conn.getInputStream(), mFileName!!, albumName)
        return true;
    }

    override fun onPostExecute(result: Boolean) {
        Log.d(TAG, "ready: " + mFileName);
        synchronized(this) {
            mComplete = true;
        }
        listener.onTaskComplete(result)
    }

    public trait OnTaskCompleteListener {
        public fun onTaskComplete(result: Boolean)
    }

    public fun isComplete() : Boolean {
        var result = false;
        synchronized(this) {
            result = mComplete;
        }
        return result;
    }
}